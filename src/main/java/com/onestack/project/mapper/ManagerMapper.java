package com.onestack.project.mapper;

import java.sql.Timestamp;
import java.util.List;

import com.onestack.project.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ManagerMapper {
	
	public List<Member> getAllMember();

	List<MemProPortPaiCate> getMemProPortPaiCate();

	void updateProStatus(@Param("proNo") int proNo, 
            @Param("professorStatus") Integer professorStatus, 
            @Param("screeningMsg") String screeningMsg);

	void updateMember(int memberNo, String name, String nickname,
					  String memberId, String email, String phone,
					  String address, String address2, int memberType,
					  int memberStatus, Timestamp banEndDate);

	public Member getWithdrawalMember();

	public Member getMember();

	void addReports(Reports reports);

	// 기간 정지 자동 해제
	void releaseSuspendMember();

	// 신고 대상 신고 횟수 증가
	int incrementReportedCount(int memberNo);

	// 신고 리스트 조회
	List<Reports> getReports();

	// 게시물 비활성화 처리
	int disableTarget(@Param("type") String type, @Param("targetId") int targetId);

	void disableCommunity(@Param("communityBoardNo") int communityBoardNo);
	void disableCommunityReply(@Param("communityReplyNo") int communityReplyNo);
	void disableQna(@Param("qnaBoardNo") int qnaBoardNo);
	void disableQnaReply(@Param("qnaReplyNo") int qnaReplyNo);
	void disableReview(@Param("reviewNo") int reviewNo);

	// 신고 처리 상태 업데이트
	void updateReportsStatus(@Param("reportsNo") int reportsNo, @Param("status") int status);
}
