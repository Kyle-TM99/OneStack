package com.onestack.project.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onestack.project.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
	public void updateMember(int memberNo, String name, String nickname, String memberId, String email, String phone, String address, String address2, int memberType, int memberStatus, Timestamp banEndDate) {
		managerMapper.updateMember(memberNo, name, nickname, memberId, email, phone, address, address2, memberType, memberStatus, banEndDate);
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

	// 회원 기간 정지 자동 해제
	@Scheduled(cron = "0 0 0 * * ?") // 00:00에 초기화
	public void releaseSuspendMember(){
		managerMapper.releaseSuspendMember();
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
