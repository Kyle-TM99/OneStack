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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityService {

    @Autowired
    private CommunityMapper communityMapper;

    //자유게시판 리스트 조회
    public List<Community> communityList() {
        return communityMapper.communityList();
    }

    // 자유게시판 상세보기 조회
    public Community getCommunity(int communityBoardNo) {
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

    // 커뮤니티 게시글 목록 조회
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
    }



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