package com.onestack.project.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public void updateProStatus(int proNo, Integer professorStatus, String screeningMsg) {
		try {
			// ✅ 1. 전문가 승인/거부 상태 업데이트
			managerMapper.updateProStatus(proNo, professorStatus, screeningMsg);

			// ✅ 2. 회원 타입 업데이트 (승인: 전문가 유지, 거부: 일반회원으로 변경)
			if (professorStatus == 0) {
				managerMapper.updateMemberType(proNo, 3);
			} else if (professorStatus == 1) {
				managerMapper.updateMemberType(proNo, 1);
			}
		} catch (Exception e) {
			log.error("전문가 심사 업데이트 중 오류 발생: ", e);
			throw new RuntimeException("전문가 심사 업데이트 중 오류가 발생했습니다.");
		}
	}

	@Transactional
	public void rejectAndDeleteProfessional(int proNo) {
		// ✅ 1. 해당 전문가의 포트폴리오 삭제
		managerMapper.deletePortfolioByProNo(proNo);

		// ✅ 2. 전문가 고급 정보 삭제
		managerMapper.deleteProfessionalAdvancedInfoByProNo(proNo);

		// ✅ 3. 전문가 삭제
		managerMapper.deleteProfessional(proNo);

		// ✅ 4. 전문가의 회원 타입을 일반 회원(1)으로 변경
		managerMapper.updateMemberType(proNo, 1);
	}


//	// 전문가 심사
//	public void updateProStatus(int proNo, Integer professorStatus, String screeningMsg) {
//		try {
//			// 전문가 승인/거부 업데이트
//			managerMapper.updateProStatus(proNo, professorStatus, screeningMsg);
//			// Member 테이블 memberType 업데이트 (승인: 1, 거부: 0)
//			managerMapper.updateMemberType(proNo, professorStatus);
//		} catch (Exception e) {
//			log.error("전문가 심사 업데이트 중 오류 발생: ", e);
//			throw new RuntimeException("전문가 심사 업데이트 중 오류가 발생했습니다.");
//		}
//	}
	
	// 회원 유형/상태 변경
	public void updateMember(Member member) {
		Map<String, Object> params = new HashMap<>();
		params.put("memberNo", member.getMemberNo());
		params.put("memberId", member.getMemberId());
		params.put("nickname", member.getNickname());
		params.put("email", member.getEmail());
		params.put("phone", member.getPhone());
		params.put("name", member.getName());
		params.put("address", member.getAddress());
		params.put("address2", member.getAddress2());
		params.put("memberType", member.getMemberType());
		params.put("memberStatus", member.getMemberStatus());

		// `banEndDate` 변환 (String → Timestamp) 포맷 오류 해결
		if (member.getMemberStatus() == 1 && member.getBanEndDate() != null) {
			try {
				// `Timestamp` → `String` 변환
				String banEndDateStr = member.getBanEndDate().toString().split(" ")[0]; // "yyyy-MM-dd"

				//`String` → `LocalDate`
				LocalDate localDate = LocalDate.parse(banEndDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

				//`LocalDate` → `LocalDateTime` (23:59:59 추가)
				LocalDateTime localDateTime = localDate.atTime(23, 59, 59);

				//`LocalDateTime` → `Timestamp` 변환
				Timestamp banEndDate = Timestamp.valueOf(localDateTime);

				params.put("banEndDate", banEndDate);
			} catch (Exception e) {
				throw new RuntimeException("정지 종료일 형식이 올바르지 않습니다. (yyyy-MM-dd)");
			}
		} else {
			params.put("banEndDate", null);
		}

		System.out.println("최종 업데이트 데이터: " + params);

		// MyBatis 호출
		managerMapper.updateMember(params);
	}

	public List<Member> getWithdrawalMembers() {
		List<Member> members = managerMapper.getWithdrawalMembers();
		log.info("탈퇴 회원 수: {}", members.size());
		return members;
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


	public void disableTarget(String type, int targetId, int reportsNo) {
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
		managerMapper.updateReportsStatus(reportsNo, 1);
	}

}
