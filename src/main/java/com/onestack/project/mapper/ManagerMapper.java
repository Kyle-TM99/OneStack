package com.onestack.project.mapper;

import java.util.List;

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


	int updateQnAStatus(@Param("no") int no, @Param("status") int status);

	int updatePostActivation(@Param("no") int no, @Param("status") int status);

	int updateCommentActivation(@Param("no") int no, @Param("status") int status);

	int updateReviewActivation(@Param("no") int no, @Param("status") int status);

	List<Reports> getReportedMember();

}
