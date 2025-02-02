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
    public String getCommunityDetail(@PathVariable int communityBoardNo, Model model) {
        Community community = communityService.getCommunity(communityBoardNo, true);
        // community.communityBoardContent에는 Quill에서 생성된 HTML이 저장되어 있어야 함
        model.addAttribute("community", community);
        return "board/communityDetail";
    }

    @PostMapping("/community/recommend")
    @ResponseBody
    public ResponseEntity<?> handleRecommend(
            @RequestParam int communityBoardNo,
            @RequestParam String recommendType,
            @RequestParam boolean isCancel,
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
                    isCancel,
                    member.getMemberNo()
            );

            if ((boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "서버 오류가 발생했습니다."));
        }
    }

    @DeleteMapping("/community/reply")
    @ResponseBody
    public ResponseEntity<?> deleteReply(@RequestParam("communityReplyNo") int communityReplyNo, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            communityService.deleteCommunityReply(communityReplyNo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/community/reply")
    @ResponseBody
    public ResponseEntity<?> updateCommunityReply(@RequestBody CommunityReply communityReply, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // 권한 체크 추가
            if (member.getMemberNo() != communityReply.getMemberNo()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            communityService.updateCommunityReply(communityReply);
            return ResponseEntity.ok(communityReply);  // 수정된 댓글 정보 반환
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
            Model model, HttpSession session,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "type", defaultValue = "null") String type,
            @RequestParam(value = "keyword", defaultValue = "null") String keyword) {

        Map<String, Object> result = communityService.getCommunityDetail(communityBoardNo);

        // communityBoardNo가 null인 경우 pathVariable에서 가져오기
        if (communityBoardNo == null) {
            communityBoardNo = pathVariableCommunityBoardNo;
        }

        // 커뮤니티 상세 정보 가져오기
        Community community = communityService.getCommunity(communityBoardNo, true);
        if (community == null) {
            return "error"; // 오류 페이지로 리다이렉트
        }

        model.addAttribute("community", community);
        model.addAttribute("htmlContent", result.get("htmlContent"));
        model.addAttribute("pageNum", pageNum);

        // 검색 옵션 처리
        boolean searchOption = !(type.equals("null") || keyword.equals("null"));
        model.addAttribute("searchOption", searchOption);
        if (searchOption) {
            model.addAttribute("type", type);
            model.addAttribute("keyword", keyword);
        }

        // 세션에서 member 가져오기
        Member member = (Member) session.getAttribute("member");
        model.addAttribute("member", member); // 모델에 member 추가

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
                                      @RequestParam(value="pageNum", defaultValue="1") int pageNum,
                                      @RequestParam(value="type", defaultValue="null") String type,
                                      @RequestParam(value="keyword", defaultValue="null") String keyword) {
        Community community = communityService.getCommunity(communityBoardNo, false);
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
    public String deleteCommunity(RedirectAttributes reAttrs, HttpSession session, @RequestParam("communityBoardNo") int communityBoardNo,
                                  @RequestParam(value="pageNum", defaultValue="1") int pageNum,
                                  @RequestParam(value="type", defaultValue="null") String type,
                                  @RequestParam(value="keyword", defaultValue="null") String keyword) {
        Member member = (Member) session.getAttribute("member");
        // 게시글 삭제
        communityService.deleteCommunity(communityBoardNo, member.getMemberNo());
        boolean searchOption = (type.equals("null") || keyword.equals("null")) ? false : true;

        reAttrs.addAttribute("pageNum", pageNum);
        reAttrs.addAttribute("searchOption", searchOption);

        if(searchOption) {
            reAttrs.addAttribute("type", type);
            reAttrs.addAttribute("keyword", keyword);
        }


        return "redirect:/community";
    }



   /* // 게시글 저장
    @PostMapping("/api/posts")
    public Long savePost(@RequestBody final CommunityRequest params) {
        return communityService.savePost(params);
    }

    // 게시글 상세정보 조회
    @GetMapping("/api/posts/{id}")
    public CommunityResponse findPostById(@PathVariable final Long id) {
        return communityService.findPostById(id);
    }

    // 게시글 목록 조회
    @GetMapping("/api/posts")
    public List<CommunityResponse> findAllPost() {
        return communityService.findAllPost();
    }*/

    // 파일을 업로드할 디렉터리 경로
    private final String uploadDir = Paths.get("C:", "tui-editor", "upload").toString();

    /**
     * 에디터 이미지 업로드
     * @param image 파일 객체
     * @return 업로드된 파일명
     */
    @PostMapping("/tui-editor/image-upload")
    public String uploadEditorImage(@RequestParam final MultipartFile image) {
        if (image.isEmpty()) {
            return "";
        }

        String orgFilename = image.getOriginalFilename();                                         // 원본 파일명
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");           // 32자리 랜덤 문자열
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);  // 확장자
        String saveFilename = uuid + "." + extension;                                             // 디스크에 저장할 파일명
        String fileFullPath = Paths.get(uploadDir, saveFilename).toString();                      // 디스크에 저장할 파일의 전체 경로

        // uploadDir에 해당되는 디렉터리가 없으면, uploadDir에 포함되는 전체 디렉터리 생성
        File dir = new File(uploadDir);
        if (dir.exists() == false) {
            dir.mkdirs();
        }

        try {
            // 파일 저장 (write to disk)
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return saveFilename;

        } catch (IOException e) {
            // 예외 처리는 따로 해주는 게 좋습니다.
            throw new RuntimeException(e);
        }
    }

    /**
     * 디스크에 업로드된 파일을 byte[]로 반환
     * @param filename 디스크에 업로드된 파일명
     * @return image byte array
     */
    @GetMapping(value = "/image-print", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public byte[] printEditorImage(@RequestParam final String filename) {

        // 업로드된 파일의 전체 경로
        String fileFullPath = Paths.get(uploadDir, filename).toString();

        // 파일이 없는 경우 예외 throw
        File uploadedFile = new File(fileFullPath);
        if (uploadedFile.exists() == false) {
            throw new RuntimeException();
        }

        try {
            // 이미지 파일을 byte[]로 변환 후 반환
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
            return imageBytes;

        } catch (IOException e) {
            // 예외 처리는 따로 해주는 게 좋습니다.
            throw new RuntimeException(e);
        }
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

    // 커뮤니티 게시글 목록 조회
    @GetMapping("/community")
    public String communityList(Model model, @RequestParam(required = false) String type, @RequestParam(required = false) String keyword) {
        Map<String, Object> data = communityService.communityList(1, type, keyword);
        model.addAllAttributes(data);
        return "board/community";
    }



    /*
        // 커뮤니티 게시글 상세 조회
        @GetMapping("community/{communityBoardNo}")
        public String communityDetail(
                @PathVariable int communityBoardNo,
                Model model
        ) {
            MemberWithCommunity communityDetail = communityService.getCommunityDetail(communityBoardNo);
            Map<String, Object> adjacentPosts = communityService.getAdjacentPosts(communityBoardNo);

            model.addAttribute("community", communityDetail);
            model.addAttribute("previousPost", adjacentPosts.get("previousPost"));
            model.addAttribute("nextPost", adjacentPosts.get("nextPost"));
            return "board/communityDetail";
        }
    */
    // 게시글 작성 폼
// 게시글 작성 폼
    @GetMapping("/communityWriteForm")
    public String communityWriteForm(Model model) {
        Community community = new Community(); // 새로운 Community 객체 생성
        model.addAttribute("community", community); // 모델에 추가
        return "board/communityWriteForm";
    }
/*
    // 게시글 등록
    @PostMapping("/write")
    public String communityWrite(@ModelAttribute Community community,
                                 @RequestParam("communityFile") MultipartFile file,
                                 HttpSession session) {
        Integer memberNo = (Integer) session.getAttribute("memberNo");
        community.setMemberNo(memberNo); // 세션에서 memberNo 가져오기
        try {
            communityService.addCommunity(community, file);
        } catch (IOException e) {
            // 예외 처리 로직 추가 (예: 로그 기록, 사용자에게 오류 메시지 표시 등)
            log.error("게시글 작성 중 오류 발생: {}", e.getMessage());
            return "redirect:board/communityWriteForm"; // 오류 발생 시 작성 폼으로 리다이렉트
        }
        return "redirect:board/community"; // 작성 후 커뮤니티 목록으로 리다이렉트
    }*/
}