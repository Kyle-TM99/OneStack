package com.onestack.project.service;

import com.onestack.project.domain.*;
import com.onestack.project.mapper.CommunityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CommunityService {

    @Autowired
    private CommunityMapper communityMapper;

    // 페이징네이션 관련 상수
    private static final int PAGE_SIZE = 10;
    private static final int PAGE_GROUP = 10;


    /* 게시글 번호 별 댓글 수 조회 */
    public int replyCount(int communityBoardNo){
        return communityMapper.replyCount(communityBoardNo);
    }

    @Transactional
    public Map<String, Object> handleRecommend(int communityBoardNo, String recommendType, int memberNo) {
        // 게시글 조회
        Community community = communityMapper.getCommunity(communityBoardNo);

        // 본인 게시글 체크
        if (community.getMemberNo().equals(memberNo)) {
            throw new IllegalStateException("자신의 게시글에는 추천할 수 없습니다.");
        }

        // 현재 추천 상태 확인
        String currentRecommendType = communityMapper.getMemberRecommendType(communityBoardNo, memberNo);

        if (currentRecommendType == null) {
            // 새로운 추천
            if ("LIKE".equals(recommendType)) {
                communityMapper.increaseLike(communityBoardNo);
            } else {
                communityMapper.increaseDislike(communityBoardNo);
            }
            communityMapper.insertRecommend(communityBoardNo, memberNo, recommendType);
        } else if (currentRecommendType.equals(recommendType)) {
            // 같은 유형 클릭 시 추천 취소
            if ("LIKE".equals(recommendType)) {
                communityMapper.decreaseLike(communityBoardNo);
            } else {
                communityMapper.decreaseDislike(communityBoardNo);
            }
            communityMapper.deleteRecommend(communityBoardNo, memberNo);
        } else {
            // 다른 유형으로 변경
            if ("LIKE".equals(recommendType)) {
                communityMapper.decreaseDislike(communityBoardNo);
                communityMapper.increaseLike(communityBoardNo);
            } else {
                communityMapper.decreaseLike(communityBoardNo);
                communityMapper.increaseDislike(communityBoardNo);
            }
            communityMapper.deleteRecommend(communityBoardNo, memberNo);
            communityMapper.insertRecommend(communityBoardNo, memberNo, recommendType);
        }

        // 업데이트된 정보 조회
        Community updatedCommunity = communityMapper.getCommunity(communityBoardNo);

        return Map.of(
                "success", true,
                "likeCount", updatedCommunity.getCommunityBoardLike(),
                "dislikeCount", updatedCommunity.getCommunityBoardDislike(),
                "currentRecommendType", recommendType
        );
    }

    public void addCommunityReply(CommunityReply communityReply) {
        communityMapper.insertCommunityReply(communityReply);
    }

    public void updateCommunityReply(CommunityReply communityReply) {
        communityMapper.updateCommunityReply(communityReply);
    }

    public void deleteCommunityReply(int communityReplyNo, int memberNo) {
        communityMapper.deleteCommunityReply(communityReplyNo, memberNo);
    }




    public Map<String, Object> getCommunityDetail(int communityBoardNo) {
        Map<String, Object> result = new HashMap<>();

        try {
            Community community = communityMapper.getCommunityDetail(communityBoardNo);
            if (community == null) {
                return result;  // 게시글이 없는 경우 빈 Map 반환
            }

            result.put("community", community);
            result.put("nickname", community.getNickname());  // 작성자 닉네임 추가

        } catch (Exception e) {
            // 예외 처리
        }

        return result;
    }

    // Reply
    public List<Community> replyList(int communityBoardNo) {
        return communityMapper.getCommunityAll(communityBoardNo);
    }



    //자유게시판 리스트 조회
    public Map<String, Object> communityList(int pageNum, String type, String keyword, String order) {
        // type이 null일 경우 기본값 설정
        if (type == null) {
            type = "communityBoardTitle";
        }

        // keyword가 null일 경우 기본값 설정
        if (keyword == null) {
            keyword = "";
        }

        boolean searchOption = (type != null && !type.isEmpty() && !type.equals("null")
                && keyword != null && !keyword.isEmpty() && !keyword.equals("null"));

        int currentPage = pageNum;
        int startRow = (currentPage - 1) * PAGE_SIZE;

        // 파라미터 Map 생성
        Map<String, Object> params = new HashMap<>();
        params.put("startRow", startRow);
        params.put("num", PAGE_SIZE);
        params.put("type", type);
        params.put("keyword", keyword);
        params.put("order", order);

        // Mapper 호출
        List<Community> communityList = communityMapper.communityList(params);
        int listCount = communityMapper.getCommunityCount(type, keyword);

        int pageCount = listCount / PAGE_SIZE + (listCount % PAGE_SIZE == 0 ? 0 : 1);
        int startPage = (currentPage / PAGE_GROUP) * PAGE_GROUP + 1
                - (currentPage % PAGE_GROUP == 0 ? PAGE_GROUP : 0);
        int endPage = startPage + PAGE_GROUP - 1;

        if (endPage > pageCount) {
            endPage = pageCount;
        }

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("cList", communityList);
        modelMap.put("pageCount", pageCount);
        modelMap.put("startPage", startPage);
        modelMap.put("endPage", endPage);
        modelMap.put("currentPage", currentPage);
        modelMap.put("listCount", listCount);
        modelMap.put("pageGroup", PAGE_GROUP);
        modelMap.put("searchOption", searchOption);
        modelMap.put("order", order);

        if(searchOption) {
            modelMap.put("type", type);
            modelMap.put("keyword", keyword);
        }

        return modelMap;
    }

    // 자유게시판 상세보기 조회
    public Community getCommunity(int communityBoardNo, boolean isCount, int memberNo) {
        if(isCount) {
            communityMapper.incrementReadCount(communityBoardNo, memberNo);
        }
        return communityMapper.getCommunity(communityBoardNo);
    }





    // 자유게시판 게시글 작성
    public void addCommunity(Community community, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString() + "_" + originalFilename;
            String uploadDir = "src/main/java/resources/static/images"; // 실제 업로드 디렉토리 경로 설정
            File dest = new File(uploadDir, filename);
            file.transferTo(dest);
            community.setCommunityFile(filename);
        }
        communityMapper.insertCommunity(community);
    }


    // 자유게시판 게시글 수정
    public int updateCommunity(Community community) throws IOException {
        return communityMapper.updateCommunity(community);
    }

    // 자유게시판 게시글 삭제
    public void deleteCommunity(int communityBoardNo, int memberNo) {
        communityMapper.deleteCommunity(communityBoardNo, memberNo);
    }

}