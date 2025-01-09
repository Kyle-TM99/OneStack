package com.onestack.project.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.onestack.project.domain.Member;

@Mapper
public interface MemberMapper {
	
	// 로그인을 위한 MemebrId 불러오기
	public Member getMember(String memberId);
	
	// 회원가입
	public int insertMember(Member member); // void -> int 로 변경
	
	// 아이디 중복 체크
	public int checkMemberId(String memberId);
	
	public int checkNickname(String nickname);
	
	public int checkEmail(String email);
	
	public int checkPhone(String phone);
	
	public String findMemberId(Member member); 
}
