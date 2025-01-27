package com.onestack.project.mapper;

import java.util.List;
import java.util.Map;

import com.onestack.project.domain.Reports;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.onestack.project.domain.MemProPortPaiCate;
import com.onestack.project.domain.Member;

@Mapper
public interface ManagerMapper {
	
	public List<Member> getAllMember();

	List<MemProPortPaiCate> getMemProPortPaiCate();

	void updateProStatus(@Param("proNo") int proNo, 
            @Param("professorStatus") Integer professorStatus, 
            @Param("screeningMsg") String screeningMsg);

	void updateMember(@Param("memberNo") int memberNo,
					  @Param("memberType") Integer memberType,
					  @Param("memberStatus") Integer memberStatus);

	public Member getWithdrawalMember();

	public Member getMember();

	boolean existsInCommunity(@Param("typeId") int typeId);
	boolean existsInQna(@Param("typeId") int typeId);
	boolean existsInReply(@Param("typeId") int typeId);
	boolean existsInReview(@Param("typeId") int typeId);

	// 신고 정보 저장
	void addReports(Reports reports);

	// 신고 대상 신고 횟수 증가
	int incrementReportedCount(@Param("memberNo") int memberNo);


	// QnA 게시글 비활성화
	int updateQnaActivation(Map<String, Object> params);

	// Community 게시글 비활성화
	int updatePostActivation(Map<String, Object> params);

	// Community 댓글 비활성화
	int updateCommentActivation(Map<String, Object> params);

	// QnA 댓글 비활성화
	int updateQnaReplyActivation(Map<String, Object> params);

	// 리뷰 비활성화
	int updateReviewActivation(Map<String, Object> params);


	List<Reports> getReportedMember();

}
