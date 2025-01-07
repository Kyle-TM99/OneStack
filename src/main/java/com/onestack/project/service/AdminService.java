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
	
	public List<Member> getAllMember() {
		return managerMapper.getAllMember();
	}
	
	public List<MemProPortCate> getMemProPortCate(){
		return managerMapper.getMemProPortCate();
	}
	public void updateProStatus(int proNo, Integer status, String screeningMsg) {
	    managerMapper.updateProStatus(proNo, status, screeningMsg);
	}

}
