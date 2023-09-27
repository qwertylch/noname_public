package com.noname.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.noname.dto.ImageDTO;
import com.noname.dto.MemberAuctionDTO;
import com.noname.dto.MemberProductDTO;
import com.noname.entity.Member;
import com.noname.enums.ImageType;
import com.noname.security.CustomUser;
import com.noname.service.FileService;
import com.noname.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {
	
	
	private final MemberService memberService;
	private final FileService fileService;
	
	@Autowired
	public MemberController(MemberService memberService, FileService fileService) {
		this.memberService = memberService;
		this.fileService = fileService;
	}
	

	// 회원 전체 목록
	@GetMapping
	public String members(Model model) {
		List<Member> members = memberService.findMembers();
		model.addAttribute("members", members);
		return "member/memberList";
	}
	// 회원 상세 페이지
	@GetMapping("/{email}")
	public String member(@PathVariable("email") String email, Model model) {
		log.info("member email : {}", email);
		Member findMember = memberService.findByEmail(email);
		model.addAttribute("member", findMember);
		return "member/member";
	}

	// 회원 정보 수정
	@GetMapping("/{email}/edit")
	public String editForm(@PathVariable String email, Model model) {
		log.info("edit form email : {}", email);
		Member findMember = memberService.findByEmail(email);
		model.addAttribute("member", findMember);
		return "member/editForm";
	}
	@PostMapping("/{email}/edit")
	public String editPro(@PathVariable String email, @ModelAttribute Member member) {
		log.info("edit pro member : {}", member);
		Member findMember = memberService.findByEmail(email);
		if(findMember.getPassword().equals(member.getPassword())) {
			// 비번 맞으면 수정
			memberService.update(member);
			// 상세 페이지로 이동
			return "redirect:/members/{email}"; // 리턴되는 경로의 {id}값은 매개변수 @PathVariable이 자동으로 체워준다.
		}else{
			// 비번 틀리면 다시 폼페이지로 -> 리다이렉트 아닌 html바로 보여주기
			// 왜? 사용자가 수정한 버전의 값이 매개변수로 넘어왔고, 수정 폼으로 리다이렉트를 시키면 수정했던 내용이 사라짐
			// 방지하고자, 사용자가 수정한 버전으로 입력한 값을 다시 뷰로 넘기고(@ModelAttribute)
			// 리다이렉트 하지않고 바로 html을 보여준다. html에서는 수정하려고 작성한 내용을 다시 화면에 뿌려주므로
			// 수정하려고 변경한 내용이 화면에 그대로 살아있다.
			return "member/editForm";
		}
	}

	//회원 삭제
	@GetMapping("/{email}/delete")
	public String deleteForm(@PathVariable String email, Model model) {
		Member findMember = memberService.findByEmail(email);
		model.addAttribute("member", findMember);
		return "member/deleteForm";
	}
	@PostMapping("/{email}/delete")
	public String deletePro(Member member, @PathVariable String email, RedirectAttributes rttr){
		Member findMember = memberService.findByEmail(email);
		if(findMember != null && findMember.getPassword().equals(member.getPassword())){
			memberService.remove(email);
			rttr.addFlashAttribute("deleteStatus", true);
			// html에서 꺼낼때 ${deleteStatus}
			return "redirect:/"; // 회원 삭제후 메인으로 이동
		}else {
			rttr.addFlashAttribute("deleteFail", true);
			return "redirect:/members/{email}/delete";
		}
	}


	@GetMapping("/home")
	public String getMemberAuction(@AuthenticationPrincipal CustomUser user, Model model) {

		if(user == null ) {
			return "redirect:/login";
		}
		
		MemberAuctionDTO memberDTO= memberService.getMemberAuction(user.getMember().getMemberId());
		log.info("Member Auction Detail {}", memberDTO);
		model.addAttribute("member", memberDTO);
		
		return "mypage/home";
	}
	
	@ResponseBody
	@PostMapping("/image")
	public ResponseEntity<ImageDTO> uploadMemberImage(@AuthenticationPrincipal CustomUser user, MultipartFile uploadImage) {
	
		ImageDTO image = fileService.uploadImage(uploadImage, ImageType.USER);
		Long imageId= memberService.saveMemberImage(user.getMember().getMemberId(), image);
		image.setImageId(imageId);
		
		return ResponseEntity.status(HttpStatus.OK).body(image);
	}
	
	@ResponseBody
    @GetMapping("/image")
    public ResponseEntity<Resource> getImage(@RequestParam String filename) throws IOException {
        log.info("fileName: {}", filename);
        log.info("URLDecoder {}", URLDecoder.decode(filename));
        File file = new File(ImageType.USER.getUploadPath(), filename);
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        byte[] imageBytes = fileService.getImage(filename, ImageType.USER);
        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        String contentType = determineContentType(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
	
	@ResponseBody
	@DeleteMapping(value = "/image/{imageId}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> removeMemberImage(@PathVariable Long imageId) {

		
		boolean result = memberService.deleteMemberImage(imageId);
	
		return  result ? ResponseEntity.status(HttpStatus.OK).body("이미지가 삭제 되었습니다.")
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청.");
	}
	

	
	
    
    private String determineContentType(String filename) {
        // 파일 확장자를 기반으로 MIME 타입을 결정하는 로직
        // 예: jpg, jpeg, png, gif 등
        // 필요에 따라 파일 확장자와 MIME 타입의 매핑을 더욱 정교하게 관리할 수 있음
        String extension = getExtension(filename);
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            default:
                return "application/octet-stream"; // 기본값으로 바이너리 타입 사용
        }
    }
    
    private String getExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
	

}
