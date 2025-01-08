package com.onestack.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.onestack.project.domain.MemProPortCate;
import com.onestack.project.domain.Member;
import com.onestack.project.service.AdminService;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class AdminController {
	@Autowired
	private AdminService adminService;

	
	 @GetMapping("/adminPage")
	 public String adminPage() {
	 return "layouts/admin_layout"; // admin_layout.html을 기본 템플릿으로 사용 
	 }
	 
	@GetMapping("/members")
    public String getMembersDashboard(Model model) {
		List<Member> member = adminService.getAllMember();		
		model.addAttribute("member", member);
        return "adminDashboard/membershipManagement/members";
    }

    @GetMapping("/suspendedMembers")
    public String getSuspendedDashboard() {
        return "adminDashboard/membershipManagement/suspendedMembers";
    }

    @GetMapping("withdrawnMembers")
    public String getWithdrawnDashboard() {
        return "adminDashboard/membershipManagement/withdrawnMembers";
    }
    
    @GetMapping("/reviewPendingInquiry")
	public String getReviewPendingInquiry(Model model) {
    	List<MemProPortCate> pList = adminService.getMemProPortCate();
    	model.addAttribute("pro", pList);
		return "adminDashboard/screeningManagement/reviewPendingInquiry";
	}
    
    @PostMapping("/reviewPro")
    public ResponseEntity<String> reviewPro(@RequestBody Map<String, Object> request) {
        try {
            int proNo = Integer.parseInt(request.get("pro").toString());
            Integer professorStatus = Integer.parseInt(request.get("professorStatus").toString());
            String screeningMsg = (String) request.get("screeningMsg");
            
            adminService.updateProStatus(proNo, professorStatus, screeningMsg);
            return ResponseEntity.ok("심사가 완료되었습니다.");
        } catch (Exception e) {
            log.error("심사 처리 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("심사 처리 중 오류가 발생했습니다.");
        }
    }


    
    @GetMapping("/reviewProcessingDetails")
	public String getReviewProcessingDetails() {
		return "adminDashboard/screeningManagement/reviewProcessingDetails";
	}
    
    @GetMapping("/reportApplicationInquiry")
    public String getReviewCompletedInquiry() {
        return "adminDashboard/reportManagement/reportApplicationInquiry";
    }
    
    @GetMapping("/manageReportdPosts")
    public String getmanageReportdPosts() {
    	return "adminDashboard/reportManagement/manageReportdPosts";
    }
    
    @GetMapping("/reportProcessingDetails")
    public String getReportProcessingDetails() {
    	return "adminDashboard/reportManagement/reportProcessingDetails";
    }
}
