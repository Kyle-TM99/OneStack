package com.onestack.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.onestack.project.domain.Member;
import com.onestack.project.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// 회원 로그인 요청을 처리하고 결과를 반환하는 메서드
	public int login(String memberId, String pass) {
		int result = -1;
		Member m = memberMapper.getMember(memberId);
		if(m == null) {
			return result;
		}
		if(passwordEncoder.matches(pass, m.getPass())) {
			result = 1;
		} else { 
			result = 0;
		}
			return result;
		}
		
	// 회원 로그인 요청을 처리하기 위한 메서드
	public Member getMember(String memberId) {
		return memberMapper.getMember(memberId);
	}
	
	public int insertMember(Member member) {
		return memberMapper.insertMember(member);
	}

    public int checkMemberId(String memberId) {
        return memberMapper.checkMemberId(memberId);
    }

    public int checkNickname(String nickname) {
        return memberMapper.checkNickname(nickname);
    }
    public int checkEmail(String email) {
    	return memberMapper.checkEmail(email);
    }

    public int checkPhone(String phone) {
        return memberMapper.checkPhone(phone);
    }

    public String findMemberId(Member member) {
        
        // 아이디 찾기 실행
        return memberMapper.findMemberId(member);
    }

}
