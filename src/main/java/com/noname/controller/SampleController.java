package com.noname.controller;

import com.noname.dto.EnumTest;
import com.noname.dto.SampleDTO;
import com.noname.dto.SampleMember;
import com.noname.entity.Member;
import com.noname.entity.Product;
import com.noname.entity.ProductImage;
import com.noname.enums.ImageType;
import com.noname.repository.ProductRepository;
import com.noname.repository.SampleJpa;
import com.noname.service.SampleService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/sample")
public class SampleController {

    final static Long MEMBER_ID = 1L;
    final static Long PRODUCT_ID = 1L;

    private final SampleJpa sampleJpa;
    private SampleService sampleService;

    private ProductRepository productRepository;

    @Autowired
    public SampleController(SampleService fileServiceEx, SampleJpa sampleJpa, ProductRepository productRepository) {
        this.sampleService = fileServiceEx;
        this.sampleJpa = sampleJpa;
        this.productRepository = productRepository;
    }
    
    


    @GetMapping("/enum")
    public String getEnum(Model model) {
    	model.addAttribute("product", new EnumTest());
        return "sample/enum";
    }
    
    @PostMapping("/enum")
    public String postEnum(EnumTest product, Model model) {
    	log.info(product.toString());
    	log.info(product.getStatus().getCode());
    	log.info(product.getStatus().getTitle());
    	log.info("=====" + product.getStatus());
    	model.addAttribute("product", product);
    	
        return "sample/enum";
    }

    
    
    @GetMapping("/layout")
    public String getSampleLayout() {
    	return "layout/sample";
    }

    @GetMapping("/session")
    public String setSession(@ModelAttribute SampleMember sampleMember, Model model, HttpSession session){
        List<Member> foundMembers = sampleJpa.findAll();
        List<SampleMember> members = foundMembers.stream().map(m -> new SampleMember(m.getMemberId(), m.getName())).collect(Collectors.toList());
        model.addAttribute("members", members);
         Long sid = (Long)session.getAttribute("sid");
         if(sid != null) {
        Member member =  sampleJpa.findById(sid).get();
        sampleMember.setMemberId(member.getMemberId());
        sampleMember.setMemberName(member.getName());
         }
        model.addAttribute("sampleMember", sampleMember);
        return "sample/session";
    }
    @PostMapping("/session")
    public String getMember(@ModelAttribute SampleMember sampleMember, HttpSession session, Model model){
        List<Member> foundMembers = sampleJpa.findAll();
        List<SampleMember> members = foundMembers.stream().map(m -> new SampleMember(m.getMemberId(), m.getName())).collect(Collectors.toList());
        model.addAttribute("members", members);

        Member member = sampleJpa.findById(sampleMember.getMemberId()).get();
        sampleMember.setMemberName(member.getName());

        session.setAttribute("sid", sampleMember.getMemberId());
        model.addAttribute("sampleMember", sampleMember);

        return "sample/session";
    }
    @ResponseBody
    @DeleteMapping("/session")
    public String deleteSession(HttpSession session) {
        session.invalidate();
        return "delete";
    }


    @GetMapping("/makethum")
    public String makeThum() throws IOException {
        List<Product> all = productRepository.findAll();

        all.stream().forEach(p -> p.getProductImages().forEach(
                m -> {
                    try {
                        Thumbnails.of(new File(ImageType.PRODUCT.getUploadPath() + m.getPath(), m.getIdentifier() + m.getName()))
                                .size(500, 500)
                                .toFile(new File(ImageType.PRODUCT.getUploadPath() + m.getPath(), "th_" + m.getIdentifier() + m.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ));
        return "sample/index";
    }


    @GetMapping("/product")
    public String getProductForm() {

        return "sample/index";
    }

    @ResponseBody
    @GetMapping("/productlist")
    public ResponseEntity<?> getProductList() {
        List<SampleDTO> productList = sampleService.getProductList();

        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }


    @GetMapping("/image")
    public ResponseEntity<Resource> getImage(@RequestParam String filename) throws IOException {
        log.info("fileName: {}", filename);

        // 파일 경로 확인 및 접근 검증
        File file = new File(ImageType.PRODUCT.getUploadPath(), filename);
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 이미지 리소스 생성
        byte[] imageBytes = sampleService.getImage(filename);
        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        // MIME 타입 결정
        String contentType = determineContentType(filename);

        // 응답 생성
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
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


//    @GetMapping("/form")
//    public String getImageForm() {
//
//        return "sample/image";
//    }
//
//    @ResponseBody
//    @PostMapping("/image")
//    public ResponseEntity<Long> saveImage(MultipartFile[] uploadImage) {
//
//        sampleService.saveProductImage(uploadImage, PRODUCT_ID);
//        return ResponseEntity.status(HttpStatus.OK).body(PRODUCT_ID);
//    }
//
//    @ResponseBody
//    @GetMapping("/image/{productId}")
//    public ResponseEntity<?> getImageByProductId(@PathVariable long productId) {
//        List<ImageResponseDTO> images = sampleService.getImageByProductId(productId);
//
//        return ResponseEntity.status(HttpStatus.OK).body(images);
//    }
//









