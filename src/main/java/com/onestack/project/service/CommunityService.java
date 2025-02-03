package com.onestack.project.service;

import com.onestack.project.domain.*;
import com.onestack.project.mapper.CommunityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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


    public Map<String, Object> handleRecommend(int communityBoardNo, String recommendType, boolean isCancel, int memberNo) {
        Community community = communityMapper.getCommunity(communityBoardNo);

        // 본인 게시글 체크
        if (community.getMemberNo() == memberNo) {
            throw new IllegalStateException("본인 게시글에는 추천/비추천을 할 수 없습니다.");
        }

        try {
            if (isCancel) {
                // 추천 취소
                if (recommendType.equals("LIKE")) {
                    communityMapper.decreaseLike(communityBoardNo);
                } else {
                    communityMapper.decreaseDislike(communityBoardNo);
                }
            } else {
                // 새로운 추천
                if (recommendType.equals("LIKE")) {
                    communityMapper.increaseLike(communityBoardNo);
                } else {
                    communityMapper.increaseDislike(communityBoardNo);
                }
            }

            // 업데이트된 정보 조회
            Community updatedCommunity = communityMapper.getCommunity(communityBoardNo);

            return Map.of(
                    "success", true,
                    "likeCount", updatedCommunity.getCommunityBoardLike(),
                    "dislikeCount", updatedCommunity.getCommunityBoardDislike()
            );
        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "message", "추천/비추천 처리 중 오류가 발생했습니다."
            );
        }
    }

    public void addCommunityReply(CommunityReply communityReply) {
        communityMapper.insertCommunityReply(communityReply);
    }

    public void updateCommunityReply(CommunityReply communityReply) {
        communityMapper.updateCommunityReply(communityReply);
    }

    public void deleteCommunityReply(int communityReplyNo) {
        communityMapper.deleteCommunityReply(communityReplyNo);
    }


    public Map<String, Object> getCommunityDetail(int communityBoardNo) {
        Map<String, Object> result = new HashMap<>();

        try {
            Community community = communityMapper.getCommunityDetail(communityBoardNo);
            if (community == null) {
                return result;  // 게시글이 없는 경우 빈 Map 반환
            }

            result.put("community", community);

        } catch (Exception e) {
        }

        return result;
    }

public List<Community> getCommunityAll(int communityBoardNo) {
    return communityMapper.getCommunityAll(communityBoardNo);
}

    // Reply
    public List<Community> replyList(int communityBoardNo) {
        return communityMapper.getCommunityAll(communityBoardNo);
    }



    //자유게시판 리스트 조회
    public Map<String, Object> communityList(int pageNum, String type, String keyword) {
        // type이 null일 경우 기본값 설정
        if (type == null) {
            type = "communityBoardTitle"; // 기본 검색 타입 설정
        }

        // keyword가 null일 경우 기본값 설정
         if (keyword == null) {
             keyword = ""; // 기본 검색어 설정
         }

        boolean searchOption = (type != null && !type.isEmpty() && !type.equals("null") && keyword != null && !keyword.isEmpty() && !keyword.equals("null"));
        int currentPage = pageNum;
        int startRow = (currentPage - 1) * PAGE_SIZE;
        int listCount = communityMapper.getCommunityCount(type, keyword);
        List<Community> communityList = communityMapper.communityList(startRow, PAGE_SIZE, type, keyword);
        int pageCount = listCount / PAGE_SIZE + (listCount % PAGE_SIZE == 0 ? 0 : 1);
        int startPage = (currentPage / PAGE_GROUP) * PAGE_GROUP + 1 - (currentPage % PAGE_GROUP == 0 ? PAGE_GROUP : 0);
        int endPage = startPage + PAGE_GROUP - 1;
        if (endPage > pageCount) {
            endPage = pageCount;
        }

        Map<String, Object> modelMap = new HashMap<String, Object>();

        modelMap.put("cList", communityList);
        modelMap.put("pageCount", pageCount);
        modelMap.put("startPage", startPage);
        modelMap.put("endPage", endPage);
        modelMap.put("currentPage", currentPage);
        modelMap.put("listCount", listCount);
        modelMap.put("pageGroup", PAGE_GROUP);
        modelMap.put("searchOption", searchOption);

        if(searchOption) {
            modelMap.put("type", type);
            modelMap.put("keyword", keyword);
        }

        return modelMap;
    }

    // 자유게시판 상세보기 조회
    public Community getCommunity(int communityBoardNo, boolean isCount) {
        if(isCount) {
            communityMapper.incrementReadCount(communityBoardNo);
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

 /*   // 커뮤니티 게시글 목록 조회
    public Map<String, Object> communityList(int page, String type, String keyword) {
        Map<String, Object> params = new HashMap<>();
        int num = 10; // 페이지당 게시글 수
        int startRow = (page - 1) * num;

        params.put("startRow", startRow);
        params.put("num", num);
        params.put("type", type);
        params.put("keyword", keyword);

        List<MemberWithCommunity> communityList = communityMapper.getCommunity(params);
        int totalCount = communityMapper.getCommunityBoardCount(params);

        Map<String, Object> result = new HashMap<>();
        result.put("list", communityList);
        result.put("totalCount", totalCount);
        result.put("currentPage", page);
        result.put("totalPages", (int) Math.ceil((double) totalCount / num));

        return result;
    }*/



    // 자유게시판 게시글 수정
    public int updateCommunity(Community community) throws IOException {
        return communityMapper.updateCommunity(community);
    }

    // 자유게시판 게시글 삭제
    public void deleteCommunity(int communityBoardNo, int memberNo) {
        communityMapper.deleteCommunity(communityBoardNo, memberNo);
    }

}