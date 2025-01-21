package com.onestack.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onestack.project.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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
        log.info("member: {}", member);

		model.addAttribute("member", member);
        return "adminDashboard/membershipManagement/members";
    }
    
    // 회원 유형/상태 변경
    @PostMapping("/adminPage/updateMember")
    @ResponseBody
    public ResponseEntity<String> updateMember(@RequestBody Map<String, String> memberData) {
        try {
            // 데이터를 가져옵니다.
            int memberNo = Integer.parseInt(memberData.get("memberNo"));
            int memberType = Integer.parseInt(memberData.get("type"));
            int memberStatus = Integer.parseInt(memberData.get("status"));

            // 서비스 호출
            adminService.updateMember(memberNo, memberType, memberStatus);

           return ResponseEntity.ok("{\"message\": \"회원 정보 수정 성공\"}");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 정보 수정 실패");
        }
    }

    @GetMapping("/suspendedMembers")
    public String getSuspendedDashboard() {
        return "adminDashboard/membershipManagement/suspendedMembers";
    }

    @GetMapping("/withdrawnMembers")
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
            int proNo = Integer.parseInt(request.get("proNo").toString());
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
    
    @PostMapping("/updateReviewPro")
    public ResponseEntity<String> updateReviewPro(@RequestBody Map<String, Object> request) {
        try {
            int proNo = Integer.parseInt(request.get("proNo").toString());
            Integer professorStatus = Integer.parseInt(request.get("professorStatus").toString());
            String screeningMsg = (String) request.get("screeningMsg");
            
            adminService.updateProStatus(proNo, professorStatus, screeningMsg);
            return ResponseEntity.ok("승인수정이 완료되었습니다.");
        } catch (Exception e) {
            log.error("승인 수정 처리 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("승인 수정 중 오류가 발생했습니다.");
        }
    }


    
    @GetMapping("/reviewProcessingDetails")
	public String getReviewProcessingDetails(Model model) {
    	List<MemProPortCate> pList = adminService.getMemProPortCate();
    	model.addAttribute("pro", pList);
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
