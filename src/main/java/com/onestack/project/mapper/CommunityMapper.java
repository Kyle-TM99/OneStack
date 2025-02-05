package com.onestack.project.mapper;

import com.onestack.project.domain.Community;
import com.onestack.project.domain.CommunityReply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommunityMapper {

    Community mainCommunityList();

    /* 댓글 수 조회 */
    int replyCount(int communityBoardNo);

    void insertCommunityReply(CommunityReply communityReply);
    void updateCommunityReply(CommunityReply communityReply);
    void deleteCommunityReply(int communityReplyNo, int memberNo);

    // 회원의 추천 이력 조회
    String getMemberRecommendType(@Param("communityBoardNo") int communityBoardNo,
                                  @Param("memberNo") int memberNo);

    // 좋아요 증가
    void increaseLike(int communityBoardNo);

    // 좋아요 감소
    void decreaseLike(int communityBoardNo);

    // 싫어요 증가
    void increaseDislike(int communityBoardNo);

    // 싫어요 감소
    void decreaseDislike(int communityBoardNo);

    // 추천 이력 추가
    void insertRecommend(@Param("communityBoardNo") int communityBoardNo,
                         @Param("memberNo") int memberNo,
                         @Param("recommendType") String recommendType);

    // 추천 이력 삭제
    void deleteRecommend(@Param("communityBoardNo") int communityBoardNo,
                         @Param("memberNo") int memberNo);


    // 커뮤니티 상세 정보 및 댓글 리스트 조회
    List<Community> getCommunityAll(@Param("communityBoardNo") int communityBoardNo);


    // 자유게시판 리스트 조회
    public List<Community> communityList(Map<String, Object> params);



    // 페이징네이션을 위한 전체 게시글 수
    public int getCommunityCount(@Param("type") String type, @Param("keyword") String keyword);

    public void incrementReadCount(@Param("communityBoardNo") int communityBoardNo, @Param("memberNo") int memberNo);

    // 자유게시판 상세보기 조회
    public Community getCommunity(int communityBoardNo);

    // 자유게시판 작성
    public void insertCommunity(Community community);

    // 자유게시한 수정
    public int updateCommunity(Community community);


    // 커뮤니티 게시글 상세 조회
    Community getCommunityDetail(@Param("communityBoardNo") int communityBoardNo);


    // 커뮤니티 게시글 삭제
    int deleteCommunity(int communityBoardNo, int memberNo);
}