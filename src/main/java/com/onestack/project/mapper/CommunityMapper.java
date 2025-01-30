package com.onestack.project.mapper;

import com.onestack.project.domain.Community;
import com.onestack.project.domain.MemberWithCommunity;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface CommunityMapper {
    // 자유게시판 리스트 조회
    public List<Community> communityList();

    // 자유게시판 상세보기 조회
    public Community getCommunity(int communityBoardNo);

    // 자유게시판 작성
    public void insertCommunity(Community community);

    // 자유게시한 수정
    public int updateCommunity(Community community);

    // 자유게시판 삭제
    public int deleteCommunity(int no);



    // 커뮤니티 게시글 목록 조회
    List<MemberWithCommunity> getCommunity(Map<String, Object> params);

    // 커뮤니티 게시글 총 개수 조회
    int getCommunityBoardCount(Map<String, Object> params);

    // 커뮤니티 게시글 상세 조회
    MemberWithCommunity getCommunityDetail(int communityBoardNo);

    // 커뮤니티 게시글 작성
    int addCommunity(Community community);

    // 커뮤니티 게시글 삭제
    int deleteCommunity(int communityBoardNo, int memberNo);

    // 이전 글 조회
    Community getPreviousCommunity(int currentCommunityBoardNo);

    // 다음 글 조회
    Community getNextCommunity(int currentCommunityBoardNo);

    // 조회수 증가
    void incrementCommunityReadCount(int communityBoardNo);
}