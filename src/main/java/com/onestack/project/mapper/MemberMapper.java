package com.onestack.project.mapper;

import com.onestack.project.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {

	// 소셜 로그인 회원 선택적 업데이트
	public void updateMemberSelective(Member member);

	// 현재 비밀번호 확인
	public int checkCurrentPassword(@Param("memberNo") int memberNo, @Param("currentPassword") String currentPassword);

	// 전문가가 받은 견적 요청 리스트 Estimation (대기 상태인 것만)
	public List<Estimation> proEstimation(@Param("proNo") int proNo);

	// 회원이 요청한 견적 리스트 Estimation
	public List<Estimation> memberEstimation(int memberNo);

	// 전문가 myPage에서 포트폴리오 관리
	//public List<MemProWithPortPortImage> memProWithPortPortImage(int memberNo);

	// myPage에서 내역 조회
	public List<MemberWithProfessional> memberWithProfessional(int memberNo);

	// 회원별 작성 리뷰 조회
	public List<Review> findMyReview(int memberNo);
	public List<Review> proReview(int proNo);
	// 회원별 작성 리뷰 수 조회
	public int findMyReviewCount(int memberNo);
	public int proReviewCount(int proNo);

	// 회원별 작성 게시글 조회
	public List<MemberWithCommunity> memberMyPageCommunity(int memberNo);

	// 회원별 작성 게시글 수 조회
	public int memberMyPageCommunityCount(int memberNo);

	// 회원별 작성 질문글 수 조회
	public int memberMyPageQnACount(int memberNo);

	// 회원별 작성 댓글 수 조회
	public int memberMyPageComReplyCount(int memberNo);

	// 회원별 작성 답글 수 조회
	public int memberMyPageQnAReplyCount(int memberNo);

	// 회원별 작성 게시글 공감 조회
	public List<Community> memberMyPageCommunityLike(int memberNo);

	// 회원별 작성 댓글 공감 조회
	public List<CommunityReply> memberMyPageComReplyLike(int memberNo);

	// 회원 정보 업데이트
	public void updateMember(Member member);

	// 회원 정보 비밀번호 수정
	public void updateMemberMyPagePass(Member member);


	public List<Map<String, Object>> myPageLikedCommunity(int memberNo);

	public List<Map<String, Object>> myPageLikedQnA(int memberNo) ;

	public List<Map<String, Object>> myPageLikedCommunityReply(int memberNo);

	public List<Map<String, Object>> myPageLikedQnAReply(int memberNo);

	// 로그인을 위한 MemebrId 불러오기
	public Member getMember(String memberId);

	// 회원가입
	public int insertMember(Member member); // void -> int 로 변경

	// 아이디 중복 체크
	public int checkMemberId(String memberId);

	public int checkNickname(String nickname);

	public int checkEmail(String email);

	public int checkPhone(String phone);

	String findMemberId(Member member);

	String findSocialMemberId(Member member);

	public String memberPassCheck(String memberId);

	public int updatePasswordMember(Member member);




	public void savePasswordResetToken(PasswordResetToken token);

	public PasswordResetToken findByToken(String token);

	public void updatePassword(@Param("memberId") String memberId, @Param("newPassword") String newPassword);

	public void deletePasswordResetToken(String token);

	// LHB - 아이디로 회원번호 조회
	Integer findMemberNoByMemberId(String memberId);


	List<Portfolio> portfolio(int proNo);

	// 견적 관련 메서드들 (중복 제거)
	void updateEstimationProgress(@Param("estimationNo") int estimationNo, @Param("progress") int progress);
	
	int getEstimationCount(@Param("proNo") int proNo);
	
	List<Estimation> getEstimationsByPage(
		@Param("proNo") int proNo, 
		@Param("offset") int offset, 
		@Param("pageSize") int pageSize
	);
	
	Estimation getEstimationByNo(@Param("estimationNo") int estimationNo);
	
	Member getMemberByNo(int memberNo);
	
	void updateEstimationCheck(@Param("estimationNo") int estimationNo, @Param("estimationCheck") int estimationCheck);

	void updateEstimationPrice(@Param("estimationNo") int estimationNo, @Param("estimationPrice") int estimationPrice);

	// 전문가 수 조회
	int getProCount();

	// 회원 수 조회
	int getMemberCount();

	// 완료된 외주 수 조회
	int getMainEstimationCount();

}
