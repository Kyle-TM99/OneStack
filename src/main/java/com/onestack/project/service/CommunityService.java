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

    public void addCommunityReply(CommunityReply communityReply) {
        communityMapper.insertCommunityReply(communityReply);
    }

    public MemberWithCommunity getCommunityDetail(int communityBoardNo) {
        return communityMapper.getCommunityDetail(communityBoardNo);
    }

/*
    public List<MemberWithCommunityReply> replyList(int communityBoardNo) {
        return communityMapper.getRepliesByBoardNo(communityBoardNo);
    }
*/
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
    public int updateCommunity(Community community, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String filename = UUID.randomUUID().toString() + "_" + originalFilename;
            String uploadDir = "path/to/upload/directory"; // 실제 업로드 디렉토리 경로 설정
            File dest = new File(uploadDir, filename);
            file.transferTo(dest);
            community.setCommunityFile(filename);
        }
        return communityMapper.updateCommunity(community);
    }

    // 자유게시판 게시글 삭제
    public void deleteCommunity(int communityBoardNo, int memberNo) {
        communityMapper.deleteCommunity(communityBoardNo, memberNo);
    }



    // 이전 글, 다음 글 조회
    public Map<String, Object> getAdjacentPosts(int currentCommunityBoardNo) {
        Map<String, Object> result = new HashMap<>();
        result.put("previousPost", communityMapper.getPreviousCommunity(currentCommunityBoardNo));
        result.put("nextPost", communityMapper.getNextCommunity(currentCommunityBoardNo));
        return result;
    }
}