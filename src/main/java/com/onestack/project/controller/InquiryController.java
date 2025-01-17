package com.onestack.project.controller;

import com.onestack.project.domain.Inquiry;
import com.onestack.project.service.InquiryService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/memberInquiry")
public class InquiryController {
	
	 private static final String DEFAULT_PATH = "src/main/resources/static/files/";

    @Autowired
    private InquiryService inquiryService;

    @GetMapping
    public String getInquiry(@RequestParam(name = "startRow", defaultValue = "0") int startRow,
                             @RequestParam(name = "num", defaultValue = "10") int num,
                             Model model) {
        List<Inquiry> inquiry = inquiryService.getInquiry(startRow, num);
        model.addAttribute("inquiry", inquiry);
        
        return "inquiry/inquiryForm"; // inquiryForm.html로 이동
    }
    
    @GetMapping("/addInquiry")
    public String addInquiry() {
    	return "inquiry/inquiryWriteForm";
    }
    
    @PostMapping("/addInquiry")
    public String addInquiry(@ModelAttribute Inquiry inquiry,
    						 @RequestParam(value="file", required=false) MultipartFile file,
                            HttpSession session) throws IOException {
        
        Integer memberNo = (Integer) session.getAttribute("memberNo");
        if (memberNo == null) {
            return "redirect:/login";
        }
        
        inquiry.setMemberNo(memberNo);
        
        if (file != null && !file.isEmpty()) {
            File directory = new File(DEFAULT_PATH);
            
            if (!directory.isDirectory() && !directory.exists()) {
                directory.mkdirs();
            }

            UUID uid = UUID.randomUUID();
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String saveName = uid.toString() + "." + extension;
            
            File saveFile = new File(directory.getAbsolutePath(), saveName);
            
            file.transferTo(saveFile);
            inquiry.setInquiryFile(saveName);
        }

        inquiryService.addInquiry(inquiry);
        return "redirect:/memberInquiry";
    }
    
    @GetMapping("/{inquiryNo}")
    public String getInquiryDetails(@PathVariable int inquiryNo, Model model) {
        Inquiry inquiry = inquiryService.getInquiryById(inquiryNo);
        model.addAttribute("inquiry", inquiry);
        return "inquiry/inquiryDetails";
    }
}
