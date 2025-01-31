package com.onestack.project.controller;

import com.onestack.project.domain.*;
import com.onestack.project.service.InquiryService;
import jakarta.servlet.http.HttpServletResponse;
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

import static java.lang.System.out;

@Slf4j
@Controller
@RequestMapping("/memberInquiry")
public class InquiryController {

    @Autowired
    private InquiryService inquiryService;

    // 문의글 목록 조회
    @GetMapping
    public String getInquiry(
            @RequestParam(defaultValue = "0") int startRow,
            @RequestParam(defaultValue = "10") int num,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            Model model) {

        // 문의글 목록 조회
        List<MemberWithInquiry> inquiryList = inquiryService.getInquiry(startRow, num, type, keyword);
        model.addAttribute("inquiryList", inquiryList);

        // 페이징 정보
        int totalCount = inquiryService.getInquiryCount(type, keyword);
        int totalPages = (totalCount + num - 1) / num;

        model.addAttribute("currentPage", startRow / num + 1);
        model.addAttribute("totalPages", totalPages);
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
    public String getInquiryDetail(@PathVariable int inquiryNo, Model model) {
        Inquiry inquiry = inquiryService.getInquiryDetail(inquiryNo);
        List<InquiryAnswer> inquiryAnswer = inquiryService.getInquiryAnswer(inquiryNo);

        model.addAttribute("inquiry", inquiry);
        model.addAttribute("inquiryAnswer", inquiryAnswer);
        return "inquiry/inquiryDetail";
    }
    // 문의글 삭제
    @PostMapping("/delete")
    public String deleteInquiry(@RequestParam("inquiryNo") int inquiryNo) {
        inquiryService.deleteInquiry(inquiryNo);
        return "redirect:/memberInquiry";
    }
    @PostMapping("/update")
    public String updateInquiry(@ModelAttribute Inquiry inquiry) {
        inquiryService.updateInquiry(inquiry);
        return "redirect:/memberInquiry";
    }

    @PostMapping("/updateForm")
    public String updateBoard(Model model, @RequestParam("inquiryNo") int inquiryNo) {
        Inquiry inquiry = inquiryService.getInquiryDetail(inquiryNo);
        model.addAttribute("inquiry", inquiry);
        return "inquiry/inquiryupdateForm";
    }

}