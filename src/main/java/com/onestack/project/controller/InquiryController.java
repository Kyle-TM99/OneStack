package com.onestack.project.controller;

import com.onestack.project.domain.Inquiry;
import com.onestack.project.domain.InquiryAnswer;
import com.onestack.project.domain.ManagerWithInquiryAnswer;
import com.onestack.project.domain.MemberWithInquiry;
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
            @RequestParam(name = "startRow", defaultValue = "0") int startRow,
            @RequestParam(name = "num", defaultValue = "10") int num,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        // 문의글 목록 조회
        List<MemberWithInquiry> inquiryList = inquiryService.getInquiry(startRow, num, type, keyword);
        model.addAttribute("inquiryList", inquiryList);

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

        // 파일 유무에 따른 서비스 메서드 호출
        if (file != null && !file.isEmpty()) {
            inquiryService.addInquiry(inquiry, file);  // 파일이 있는 경우
        } else {
            inquiryService.addInquiry(inquiry);  // 파일이 없는 경우
        }
        // 성공시 목록 페이지로 리다이렉트
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
    public String updateInquiry(Inquiry inquiry) {
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