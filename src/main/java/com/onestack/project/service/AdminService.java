package com.onestack.project.service;

import java.util.List;

import com.onestack.project.domain.Reports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onestack.project.domain.MemProPortPaiCate;
import com.onestack.project.domain.Member;
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

	// 신고 대상의 타입 자동 식별
	public String identifyReportType(int targetId) {
		if (managerMapper.existsInCommunity(targetId)) {
			return "community";
		} else if (managerMapper.existsInQna(targetId)) {
			return "qna";
		} else if (managerMapper.existsInReply(targetId)) {
			return "reply";
		} else if (managerMapper.existsInReview(targetId)) {
			return "review";
		}
		return null; // 대상이 어느 테이블에도 없을 경우
	}

	// 신고 정보 저장
	public void addReports(Reports reports) {
		managerMapper.addReports(reports);
	}

	public boolean disableContent(String type, int no) {
		int rowsAffected = 0;

		switch (type) {
			case "post": // 커뮤니티 게시글 비활성화
				rowsAffected = managerMapper.updatePostActivation(no, 0); // 0: 비활성화
				break;
			case "qna": // QnA 게시글 비활성화
				rowsAffected = managerMapper.updateQnAStatus(no, 0); // 0: 비활성화
				break;
			case "comment": // 댓글 비활성화
				rowsAffected = managerMapper.updateCommentActivation(no, 0); // 0: 비활성화
				break;
			case "review": // 리뷰 비활성화
				rowsAffected = managerMapper.updateReviewActivation(no, 0); // 0: 비활성화
				break;
			default:
				throw new IllegalArgumentException("유효하지 않은 유형입니다: " + type);
		}

		return rowsAffected > 0; // 비활성화 성공 여부 반환
	}

	// 신고 대상의 신고 횟수 증가
	public boolean incrementReportedCount(int memberNo) {
		int rowsAffected = managerMapper.incrementReportedCount(memberNo);
		return rowsAffected > 0;
	}

	public List<Reports> getReportsMember() {
		return managerMapper.getReportedMember();
	}

}
