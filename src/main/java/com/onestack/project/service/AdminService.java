package com.onestack.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onestack.project.domain.MemProPortCate;
import com.onestack.project.domain.Member;
import com.onestack.project.domain.Professional;
import com.onestack.project.mapper.ManagerMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService {
	@Autowired
	private ManagerMapper managerMapper;

	// 모든 회원 조회
	public List<Member> getAllMember() {
		return managerMapper.getAllMember();
	}

	// 회원/전문가/포트폴리오/카테고리 조회
	public List<MemProPortCate> getMemProPortCate(){
		return managerMapper.getMemProPortCate();
	}

	// 전문가 심사
	public void updateProStatus(int proNo, Integer professorStatus, String screeningMsg) {
	    managerMapper.updateProStatus(proNo, professorStatus, screeningMsg);
	}
	
	// 회원 유형/상태 변경
	public void updateMember(String memberId, int memberType) {
		managerMapper.updateMember(memberId, memberType);
	}

}
