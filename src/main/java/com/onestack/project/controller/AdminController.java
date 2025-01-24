package com.onestack.project.controller;

import java.util.List;
import java.util.Map;

import com.onestack.project.domain.Reports;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import com.onestack.project.domain.MemProPortPaiCate;
import com.onestack.project.domain.Member;
import com.onestack.project.service.AdminService;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class AdminController {
	@Autowired
	private AdminService adminService;


    @GetMapping("/delete")
    public String delete() {
        return "views/deleteButton";
    }

    @PostMapping("/report")
    @ResponseBody
    public ResponseEntity<?> submitReport(@RequestBody Reports reports) {
        System.out.println("Reports Data: " + reports);

        // 허용된 ENUM 값 리스트
        List<String> validTypes = List.of("member", "community", "reply", "review");

        if (reports.getReportsTarget() == null) {
            return ResponseEntity.badRequest().body("신고 항목은 필수입니다.");
        }

        if (!validTypes.contains(reports.getReportsType())) {
            return ResponseEntity.badRequest().body("유효하지 않은 신고 유형입니다.");
        }

        try {
            adminService.addReports(reports);
            return ResponseEntity.ok("신고가 성공적으로 접수되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("신고 접수 중 오류가 발생했습니다.");
        }
    }


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
    public String getSuspendedDashboard(Model model) {
        List<Member> member = adminService.getAllMember();

        model.addAttribute("member", member);
         return "adminDashboard/membershipManagement/suspendedMembers";
    }

    @GetMapping("/withdrawnMembers")
    public String getWithdrawnDashboard(Model model) {
        Member member = adminService.getWithdrawalMember();

        model.addAttribute("member", member);
         return "adminDashboard/membershipManagement/withdrawnMembers";
    }

    @GetMapping("/reviewPendingInquiry")
    public String getReviewPendingInquiry(Model model) {
        List<MemProPortPaiCate> pList = adminService.getMemProPortPaiCate();
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
    	List<MemProPortPaiCate> pList = adminService.getMemProPortPaiCate();
    	model.addAttribute("pro", pList);
		return "adminDashboard/screeningManagement/reviewProcessingDetails";
	}
    
    @GetMapping("/reportApplicationInquiry")
    public String getReviewCompletedInquiry() {

        return "adminDashboard/reportManagement/reportApplicationInquiry";
    }
    
    @GetMapping("/manageReportdPosts")
    public String getManageReportdPosts() {
        return "adminDashboard/reportManagement/manageReportdPosts";
    }
    
    @GetMapping("/reportProcessingDetails")
    public String getReportProcessingDetails() {
    	return "adminDashboard/reportManagement/reportProcessingDetails";
    }
}
