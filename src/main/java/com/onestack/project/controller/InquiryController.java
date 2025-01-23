package com.onestack.project.controller;

import com.onestack.project.domain.Inquiry;
import com.onestack.project.domain.MemberWithInquiry;
import com.onestack.project.service.InquiryService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/memberInquiry")
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    // 기본 문의글 목록 조회 (검색 기능 포함)
    @GetMapping
    public String getInquiry(
            @RequestParam(name = "startRow", defaultValue = "0") int startRow,
            @RequestParam(name = "num", defaultValue = "10") int num,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        // 문의글 목록 조회
        List<MemberWithInquiry> inquiryList = inquiryService.getInquiry(startRow, num, type, keyword);

        // 전체 문의글 수 조회 (페이징 처리를 위해)
        int totalCount = inquiryService.getInquiryCount(type, keyword);

        model.addAttribute("inquiryList", inquiryList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPage", startRow / num + 1);
        model.addAttribute("searchType", type);
        model.addAttribute("keyword", keyword);

        return "inquiry/inquiryForm";
    }

    // 날짜 범위로 문의글 검색
    @GetMapping("/date")
    public String getInquiryByDate(
            @RequestParam(name = "startRow", defaultValue = "0") int startRow,
            @RequestParam(name = "num", defaultValue = "10") int num,
            @RequestParam("date1") String date1,
            @RequestParam("date2") String date2,
            Model model) {

        List<MemberWithInquiry> inquiryList = inquiryService.getInquiryByDate(startRow, num, date1, date2);

        Map<String, Object> params = new HashMap<>();
        params.put("type", "date");
        params.put("date1", date1);
        params.put("date2", date2);
        int totalCount = inquiryService.getInquiryCount("date", null);

        model.addAttribute("inquiryList", inquiryList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPage", startRow / num + 1);
        model.addAttribute("date1", date1);
        model.addAttribute("date2", date2);

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

        // 세션에서 회원 번호 가져오기
        Integer memberNo = (Integer) session.getAttribute("memberNo");
        log.info("세션의 memberNo: {}", memberNo);

        // 로그인 체크
        if (memberNo == null) {
            return "redirect:/login";
        }
        // 세션의 memberNo를 inquiry 객체에 설정
        inquiry.setMemberNo(memberNo);

        // 파일 유무에 따른 서비스 메서드 호출
        if (file != null && !file.isEmpty()) {
            inquiryService.addInquiry(inquiry, file);  // 파일이 있는 경우
        } else {
            inquiryService.addInquiry(inquiry);  // 파일이 없는 경우
        }
        // 성공시 목록 페이지로 리다이렉트
        return "redirect:/memberInquiry";
    }
}