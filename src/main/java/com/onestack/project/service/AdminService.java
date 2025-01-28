package com.onestack.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onestack.project.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public List<MemProPortPaiCate> getMemProPortPaiCate() {
		return managerMapper.getMemProPortPaiCate();
	}

	// 전문가 심사
	public void updateProStatus(int proNo, Integer professorStatus, String screeningMsg) {
	    managerMapper.updateProStatus(proNo, professorStatus, screeningMsg);
	}
	
	// 회원 유형/상태 변경
	public void updateMember(int memberNo, Integer memberType, Integer memberStatus) {
		managerMapper.updateMember(memberNo, memberType, memberStatus);
	}

	public Member getWithdrawalMember(){
		return managerMapper.getWithdrawalMember();
	}

	public Member getMember(){
		return managerMapper.getMember();
	}

	// 신고 접수 처리
	public void saveReport(Reports report) {
		managerMapper.addReports(report);
		managerMapper.incrementReportedCount(report.getReportedMemberNo());
	}

	// 신고 리스트 조회
	public List<Reports> getReports() {
		return managerMapper.getReports();
	}
	// 신고 횟수 증가
	public int incrementReportedCount(int memberNo) {
		return managerMapper.incrementReportedCount(memberNo);
	}


	public void disableTarget(String type, int targetId) {
		switch (type) {
			case "community":
				managerMapper.disableCommunity(targetId);
				break;
			case "communityReply":
				managerMapper.disableCommunityReply(targetId);
				break;
			case "qna":
				managerMapper.disableQna(targetId);
				break;
			case "qnaReply":
				managerMapper.disableQnaReply(targetId);
				break;
			case "review":
				managerMapper.disableReview(targetId);
				break;
			default:
				throw new IllegalArgumentException("지원되지 않는 타입입니다: " + type);
		}
	}

}
