package com.noname.service;

import java.util.List;

import com.noname.dto.ImageDTO;
import com.noname.dto.MemberAuctionDTO;
import com.noname.entity.Member;

public interface MemberService {
	

	// 회원 가입
	public Member save(Member member);
	// 회원 전체 정보 가져오기
	public List<Member> findMembers();
	// 회원 정보 email로 가져오기
	public Member findByEmail(String email);
	// 회원 정보 수정
	public void update(Member member);
	// 회원 삭제
	public void remove(String email);
	// 로그인 처리
	public Member login(Member member);
	
	public MemberAuctionDTO getMemberAuction(Long memberId);
	
	public Long saveMemberImage(Long id, ImageDTO imageDTO);
	
	public boolean deleteMemberImage(Long imageId);

}
