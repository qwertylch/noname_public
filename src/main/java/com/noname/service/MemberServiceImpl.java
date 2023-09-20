package com.noname.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noname.dto.ImageDTO;
import com.noname.dto.MemberAuctionDTO;
import com.noname.entity.Member;
import com.noname.entity.MemberImage;
import com.noname.entity.Product;
import com.noname.enums.AuctionStatus;
import com.noname.enums.ImageType;
import com.noname.enums.ProductStatus;
import com.noname.enums.Role;
import com.noname.repository.MemberImageRepository;
import com.noname.repository.MemberRepository;


@Service
public class MemberServiceImpl implements MemberService {
	

	private final MemberRepository memberRepository;
	private final MemberImageRepository imageRepository;
	private final FileService fileService;
	private final PasswordEncoder passwordEncoder;


	@Autowired
	public MemberServiceImpl(MemberRepository memberRepository
			,FileService fileService
			,MemberImageRepository imageRepository
			,PasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.imageRepository = imageRepository;
		this.fileService = fileService;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public Member save(Member member) {
		// 비밀번호 저장 전에 암호화
		member.setPassword(passwordEncoder.encode(member.getPassword()));
		member.setRole(Role.MEMBER); // 일반회원으로 가입
		return memberRepository.save(member);
	}

	@Override
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	@Override
	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	@Override
	public void update(Member member) {
		memberRepository.save(member); // 수정된 내용을 저장
	}

	@Override
	public void remove(String email) {
		Member member = memberRepository.findByEmail(email);
		if (member != null) {
			memberRepository.delete(member);
		}
	}

	@Override
	public Member login(Member member) {
		Member findMember = memberRepository.findByEmail(member.getEmail());
		if (findMember != null && passwordEncoder.matches(member.getPassword(), findMember.getPassword())) {
			return findMember;
		}
		return null;
	}
	
	@Override
	public MemberAuctionDTO getMemberAuction(Long memberId){
		
		Member member = memberRepository.findMemberWithProductsAndAuction(memberId);
		
		// 판매 내역 --  AuctionStatus / ProductSatus
		// 이하 내역 --  ProductSatus APPROVED 상태   
		// 경매 대기     경매 날짜 미도래  PENDING         
		// 경매 진행 중   경매 날짜 도래   INBID  
		// 경매 경매 종료  경매 날짜 종료  CONFIRM,  OUTBID 
		int pendingCount = 0;
		int inBidCount = 0;
		int confirmCount = 0;
		int outBidCount = 0;

		for (Product product : member.getProducts()) {
		    ProductStatus productStatus = product.getStatus();
		    AuctionStatus auctionStatus = product.getProductAuction().getStatus();
		    if (productStatus == ProductStatus.APPROVED) {
		        if (auctionStatus == AuctionStatus.PENDING) {
		            pendingCount++;
		        } else if (auctionStatus == AuctionStatus.INBID) {
		            inBidCount++;
		        } else if (auctionStatus == AuctionStatus.CONFIRM) {
		            confirmCount++;
		        } else if (auctionStatus == AuctionStatus.OUTBID) {
		            outBidCount++;
		        }
		    }
		}
		
		MemberAuctionDTO memberDTO = new MemberAuctionDTO();
		memberDTO.setMemberName(member.getName());
		memberDTO.setEmail(member.getEmail());
		MemberImage image = member.getImages();
		if(image != null) {
			memberDTO.setImage(fileService.encodeImageURL(null, image, false));
			memberDTO.setImageId(image.getImageId());
		}
		
		memberDTO.setPendingCount(pendingCount);
		memberDTO.setInBidCount(inBidCount);
		memberDTO.setEndCount(confirmCount + outBidCount);
		return memberDTO;
	}
	
	public Long saveMemberImage(Long id, ImageDTO imageDTO) {
		
		Optional<Member> member = memberRepository.findById(id);
		Long imageId = null;
		if(member.isPresent()) {
			MemberImage image = new MemberImage();
			image.setIdentifier(imageDTO.getIdentifier());
			image.setPath(imageDTO.getPath());
			image.setName(imageDTO.getName());
			image.setMember(member.get());
			imageRepository.save(image);
			imageId = image.getImageId();
		}
		
		return imageId;
	}

	@Override
	public boolean deleteMemberImage(Long imageId) {
		Optional<MemberImage> seletedImage = imageRepository.findById(imageId);
		ImageDTO imageDTO = new ImageDTO();
		boolean deleteResult = false;
		if(seletedImage.isPresent()) {
			MemberImage memberImage = seletedImage.get();
			imageDTO.setIdentifier(memberImage.getIdentifier());
			imageDTO.setPath(memberImage.getPath());
			imageDTO.setName(memberImage.getName());
			fileService.deleteImageFile(imageDTO, ImageType.USER);
			imageRepository.delete(memberImage);
			deleteResult = true;
		}
		
		return deleteResult;
	}
	
	
	
	
	

}
