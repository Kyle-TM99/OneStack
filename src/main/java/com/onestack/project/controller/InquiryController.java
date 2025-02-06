package com.onestack.project.controller;

import com.onestack.project.domain.*;
import com.onestack.project.service.InquiryService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/memberInquiry")
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    // 문의글 목록 조회
    @GetMapping
    public String getInquiry(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpSession session,
            Model model) {

        Member member = (Member) session.getAttribute("member");
        int memberNo = member.getMemberNo();
        boolean isAdmin = member.isAdmin();

        // 시작 행 번호 계산
        int startRow = (pageNum - 1) * pageSize;

        // 전체 게시글 수 조회
        int totalCount = inquiryService.getInquiryCount(memberNo, type, keyword, isAdmin);

        // 전체 페이지 수 계산 수정
        int totalPages = totalCount == 0 ? 1 : (totalCount - 1) / pageSize + 1;

        // 현재 페이지가 총 페이지 수보다 크면 첫 페이지로 리다이렉트
        if (pageNum > totalPages) {
            return "redirect:/memberInquiry?pageNum=1";
        }

        // 문의글 목록 조회
        List<MemberWithInquiry> inquiryList = inquiryService.getInquiry(memberNo, type, keyword, isAdmin, startRow, pageSize);

        model.addAttribute("inquiryList", inquiryList);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("searchOption", (type != null && !type.isEmpty() && keyword != null && !keyword.isEmpty()));
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "inquiry/inquiryForm";
    }

    // 문의글 작성 폼으로 이동
    @GetMapping("/inquiryWrite")
    public String InquiryWriteForm() {
        return "inquiry/inquiryWriteForm";
    }

    // 문의글 등록 처리
    @PostMapping("/addInquiry")
    public String addInquiry(
            @ModelAttribute Inquiry inquiry,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session) throws IOException {

        // 파일 처리 및 문의글 저장
        if (file != null && !file.isEmpty()) {
            inquiryService.addInquiry(inquiry, file);
        } else {
            inquiryService.addInquiry(inquiry);
        }

        return "redirect:/memberInquiry";
    }

    // 문의글 상세보기
    @GetMapping("/{inquiryNo}")
    public String getInquiryDetail(
            @PathVariable int inquiryNo,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            Model model) {

        // 기존 상세 조회 로직
        Inquiry inquiry = inquiryService.getInquiryDetail(inquiryNo);
        List<InquiryAnswer> inquiryAnswer = inquiryService.getInquiryAnswer(inquiryNo);

        model.addAttribute("inquiry", inquiry);
        model.addAttribute("inquiryAnswer", inquiryAnswer);

        // 이전 페이지 정보를 모델에 추가
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "inquiry/inquiryDetail";
    }

    // 문의글 삭제
    @PostMapping("/delete")
    public String deleteInquiry(@RequestParam("inquiryNo") int inquiryNo) {
        inquiryService.deleteInquiry(inquiryNo);
        return "redirect:/memberInquiry";
    }

    @PostMapping("/update")
    public String updateInquiry(@ModelAttribute Inquiry inquiry,
                                @RequestParam(defaultValue = "1") int pageNum,
                                @RequestParam(required = false) String type,
                                @RequestParam(required = false) String keyword) {
        inquiryService.updateInquiry(inquiry);
        // 수정 후 해당 게시글의 상세 페이지로 리다이렉트
        return "redirect:/memberInquiry/" + inquiry.getInquiryNo()
                + "?pageNum=" + pageNum
                + (type != null ? "&type=" + type : "")
                + (keyword != null ? "&keyword=" + keyword : "");
    }

    @PostMapping("/updateForm")
    public String updateBoard(Model model,
                              @RequestParam("inquiryNo") int inquiryNo,
                              @RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) String keyword) {
        Inquiry inquiry = inquiryService.getInquiryDetail(inquiryNo);
        model.addAttribute("inquiry", inquiry);
        // 페이징 및 검색 정보를 모델에 추가
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        return "inquiry/inquiryUpdateForm";
    }

    @PostMapping("/addInquiryAnswer")
    @ResponseBody // AJAX 요청에 대한 응답을 JSON으로 반환
    public ResponseEntity<String> addInquiryAnswer(@RequestBody InquiryAnswer inquiryAnswer, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        boolean isAdmin = member.isAdmin(); // isAdmin 값 가져오기

        inquiryService.addInquiryAnswer(inquiryAnswer, isAdmin);
        return ResponseEntity.ok("답변이 등록되었습니다.");
    }

    @GetMapping("/checkAdmin")
    @ResponseBody
    public int checkAdmin(HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            return member.isAdmin() ? 1 : 0; // isAdmin 값을 반환
        }
        return 0; // 로그인하지 않은 경우
    }

    // 문의글 만족/불만족 처리
    @PostMapping("/inquiry/satisfaction")
    public ResponseEntity<String> updateSatisfaction(@RequestParam int inquiryNo, @RequestParam boolean isSatisfied) {
        inquiryService.handleSatisfaction(inquiryNo, isSatisfied);
        return ResponseEntity.ok("문의글 상태가 업데이트되었습니다.");
    }

    @PostMapping("/memberInquiry/updateInquiryAnswer")
    public String updateInquiryAnswer(@RequestBody InquiryAnswer inquiryAnswer) {
        inquiryService.updateInquiryAnswer(inquiryAnswer);
        return "답변이 수정되었습니다.";
    }

}