package com.onestack.project.controller;

import com.onestack.project.domain.*;
import com.onestack.project.service.CommunityService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping("/detail/{communityBoardNo}")
    public String getCommunityDetail(@RequestParam(value = "communityBoardNo") int communityBoardNo,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     HttpSession session,
                                     Model model) {
        Member member = (Member) session.getAttribute("member");
        int memberNo = member != null ? member.getMemberNo() : 0;

        Community community = communityService.getCommunity(communityBoardNo, true, memberNo);
        // community.communityBoardContent에는 Quill에서 생성된 HTML이 저장되어 있어야 함
        model.addAttribute("community", community);
        model.addAttribute("pageNum", pageNum);

        return "board/communityDetail";
    }

    @PostMapping("/community/recommend")
    @ResponseBody
    public ResponseEntity<?> handleRecommend(
            @RequestParam int communityBoardNo,
            @RequestParam String recommendType,
            HttpSession session) {

        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요한 기능입니다."));
        }

        try {
            Map<String, Object> result = communityService.handleRecommend(
                    communityBoardNo,
                    recommendType,
                    member.getMemberNo()
            );

            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "서버 오류가 발생했습니다."));
        }
    }

    @PostMapping("/replyDelete")
    @ResponseBody
    public ResponseEntity<?> deleteReply(
            @RequestParam("communityReplyNo") int communityReplyNo,
            @RequestParam("memberNo") int memberNo,
            HttpSession session) {

        Member loginMember = (Member) session.getAttribute("member");

        // 로그인 체크
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 작성자 본인 확인
        if (loginMember.getMemberNo() != memberNo) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
        }

        try {
            communityService.deleteCommunityReply(communityReplyNo, memberNo);
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 중 오류가 발생했습니다.");
        }
    }

    @PatchMapping("/community/reply")
    @ResponseBody
    public ResponseEntity<?> updateCommunityReply(
            @RequestBody CommunityReply communityReply,
            HttpSession session) {

        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // 권한 체크
            if (member.getMemberNo() != communityReply.getMemberNo()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            communityService.updateCommunityReply(communityReply);
            return ResponseEntity.ok(communityReply);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/community/reply")
    @ResponseBody
    public ResponseEntity<CommunityReply> addCommunityReply(@RequestBody CommunityReply communityReply, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // 필요한 정보 설정
            communityReply.setMemberNo(member.getMemberNo());
            communityReply.setNickname(member.getNickname());  // 닉네임 설정
            communityReply.setCommunityReplyRegDate(new Timestamp(System.currentTimeMillis())); // 등록일시 설정

            // 댓글 등록
            communityService.addCommunityReply(communityReply);

            return ResponseEntity.ok(communityReply);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @GetMapping("/communityDetail")
    public String communityDetail(
            @RequestParam(value = "communityBoardNo", required = false) Integer communityBoardNo,
            @PathVariable(value = "communityBoardNo", required = false) Integer pathVariableCommunityBoardNo,
            Model model,
            HttpSession session,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "type", defaultValue = "null") String type,
            @RequestParam(value = "keyword", defaultValue = "null") String keyword) {

        // 세션 체크
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/login";
        }

        // communityBoardNo가 null인 경우 pathVariable에서 가져오기
        if (communityBoardNo == null) {
            communityBoardNo = pathVariableCommunityBoardNo;
        }

        // 커뮤니티 상세 정보 가져오기
        Community community = communityService.getCommunity(communityBoardNo, true, member.getMemberNo());
        if (community == null) {
            return "error";
        }

        // 댓글 수 조회
        int replyCount = communityService.replyCount(communityBoardNo);

        // 상세 정보 조회 (작성자 닉네임 포함)
        Map<String, Object> result = communityService.getCommunityDetail(communityBoardNo);

        model.addAttribute("replyCount", replyCount);
        model.addAttribute("community", community);
        model.addAttribute("nickname", community.getNickname());
        model.addAttribute("pageNum", pageNum);

        // 검색 옵션 처리
        boolean searchOption = !(type.equals("null") || keyword.equals("null"));
        model.addAttribute("searchOption", searchOption);
        if (searchOption) {
            model.addAttribute("type", type);
            model.addAttribute("keyword", keyword);
        }

        model.addAttribute("member", member);

        // 댓글 리스트 가져오기
        List<Community> replyList = communityService.replyList(communityBoardNo);
        model.addAttribute("replyList", replyList);

        return "board/communityDetail";
    }

    /* 자유게시판 생성 */
    @PostMapping("/addCommunity")
    public String addBoard(Community community, MultipartFile file, HttpSession session) throws IOException {
        Member member = (Member) session.getAttribute("member"); // 세션에서 memberNo 가져오기
        community.setMemberNo(member.getMemberNo()); // Community 객체에 memberNo 설정
        communityService.addCommunity(community, file);
        return "redirect:community";
    }



    /* 자유게시판 수정 */
    @GetMapping("/communityUpdateForm")
    public String communityUpdateForm(@RequestParam("communityBoardNo") int communityBoardNo, Model model,
                                      @RequestParam(value = "memberNo") int memberNo,
                                      @RequestParam(value="pageNum", defaultValue="1") int pageNum,
                                      @RequestParam(value="type", defaultValue="null") String type,
                                      @RequestParam(value="keyword", defaultValue="null") String keyword) {
        Community community = communityService.getCommunity(communityBoardNo, false, memberNo);
        boolean searchOption = (type.equals("null") || keyword.equals("null")) ? false : true;

        model.addAttribute("pageNum", pageNum);
        model.addAttribute("community", community);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("searchOption", searchOption);

        if(searchOption) {
            model.addAttribute("type", type);
            model.addAttribute("keyword", keyword);
        }

        return "board/communityUpdateForm"; // 이 경로가 맞는지 확인
    }

    /* 자유게시판 수정 */
    @PostMapping("/communityUpdate")
    public String updateCommunity(Community community, MultipartFile file, HttpSession session,
                                  @RequestParam(value="pageNum", defaultValue="1") int pageNum, RedirectAttributes reAttrs,
                                  @RequestParam(value="type", defaultValue="null") String type,
                                  @RequestParam(value="keyword", defaultValue="null") String keyword) throws IOException {
        Member member = (Member) session.getAttribute("member"); // 세션에서 member 가져오기
        community.setMemberNo(member.getMemberNo()); // Community 객체에 memberNo 설정

        // 게시글 업데이트
        communityService.updateCommunity(community);
        boolean searchOption = (type.equals("null") || keyword.equals("null")) ? false : true;

        reAttrs.addAttribute("searchOption", searchOption);
        reAttrs.addAttribute("pageNum", pageNum);

        if(searchOption) {
            reAttrs.addAttribute("type", type);
            reAttrs.addAttribute("keyword", keyword);
        }
        return "redirect:/community"; // 수정 후 커뮤니티 목록으로 리다이렉트
    }


    /* 자유게시판 삭제 */
    @PostMapping("/delete")
    @ResponseBody // URL 패턴 수정
    public Map<String, Object> deleteCommunity(@RequestParam("communityBoardNo") int communityBoardNo,
                                  @RequestParam("memberNo") int memberNo,
                                  HttpSession session) {
        Map<String,Object> response = new HashMap<>();
        // 세션 체크
        Member member = (Member) session.getAttribute("member");

        // 권한 체크 추가
        if (member == null || member.getMemberNo() != memberNo) {
            response.put("status", "nodelete");
            response.put("message", "삭제 권한이 없습니다.");
            return response;
        }

        try {
            // 게시글 삭제
            communityService.deleteCommunity(communityBoardNo, memberNo);
            response.put("status", "success");
            response.put("message", "게시글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "게시글 삭제 중 오류가 발생했습니다.");
        }
        return response;
    }

    //QIll API
    @ResponseBody
    @GetMapping(value = "/display")
    public ResponseEntity<byte[]> showImageGET(
            @RequestParam("fileName") String fileName
    ) {
        File file = new File("C:\\upload\\" + fileName);

        ResponseEntity<byte[]> result = null;

        try {

            HttpHeaders header = new HttpHeaders();

            header.add("Content-type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping("/community")
    public String communityList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "type", required = false) String type,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "order", defaultValue = "latest") String order,
                                Model model) {
        // searchOption 처리 로직 추가
        boolean searchOption = (type != null && !type.isEmpty() &&
                keyword != null && !keyword.isEmpty());

        Map<String, Object> data = communityService.communityList(pageNum, type, keyword, order);

        model.addAttribute("searchOption", searchOption);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("order", order);
        model.addAllAttributes(data);

        return "board/community";
    }

// 게시글 작성 폼
    @GetMapping("/communityWriteForm")
    public String communityWriteForm(Model model) {
        Community community = new Community(); // 새로운 Community 객체 생성
        model.addAttribute("community", community); // 모델에 추가
        return "board/communityWriteForm";
    }
}