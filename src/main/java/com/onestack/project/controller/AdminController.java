package com.onestack.project.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onestack.project.domain.QnA;
import com.onestack.project.domain.Reports;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
    public String delete(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        String memberId = member.getMemberId();
        if(member == null) {
            return "redirect:/loginForm";
        }
        model.addAttribute("member",member);
        return "views/deleteButton";
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

    @PostMapping("/adminPage/updateMember")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateMember(@RequestBody Map<String, String> memberData) {
        try {
            int memberNo = Integer.parseInt(memberData.get("memberNo"));
            String name = memberData.get("name");
            String nickname = memberData.get("nickname");
            String memberId = memberData.get("memberId");
            String email = memberData.get("email");
            String phone = memberData.get("phone");
            String address1 = memberData.get("address");
            String address2 = memberData.get("address2");
            int memberType = Integer.parseInt(memberData.get("memberType"));
            int memberStatus = Integer.parseInt(memberData.get("memberStatus"));

            // 기간 정지일 변환
            Timestamp banEndDate = null;
            if (memberStatus == 1 && memberData.containsKey("banEndDate") && !memberData.get("banEndDate").isEmpty()) {
                banEndDate = Timestamp.valueOf(memberData.get("banEndDate") + " 23:59:59");
            }

            // 서비스 호출
            adminService.updateMember(memberNo, name, nickname, memberId, email, phone, address1, address2, memberType, memberStatus, banEndDate);

            return ResponseEntity.ok(Map.of("message", "회원 정보 수정 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "회원 정보 수정 실패: " + e.getMessage()));
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

    @GetMapping("/manageReportedPosts")
    public String getManageReportedPosts(Model model) {
        List<Reports> reportsList = adminService.getReports();
        System.out.println("reportsList: " + reportsList); // 데이터를 출력해 실제로 값이 있는지 확인

        model.addAttribute("reportsList", reportsList);
        return "adminDashboard/reportManagement/manageReportedPosts";
    }
    
    @GetMapping("/reportProcessingDetails")
    public String getReportProcessingDetails() {
    	return "adminDashboard/reportManagement/reportProcessingDetails";
    }

    // 신고 접수
    @PostMapping("/reports")
    public ResponseEntity<String> report(@RequestBody Reports report) {
        adminService.saveReport(report);
        return ResponseEntity.ok("신고가 접수되었습니다.");
    }

    @PostMapping("/disable")
    public ResponseEntity<String> disableTarget(
            @RequestParam("type") String type,
            @RequestParam("targetId") int targetId) {
        try {
            adminService.disableTarget(type, targetId);
            return ResponseEntity.ok("비활성화 처리 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("비활성화 처리 중 오류 발생: " + e.getMessage());
        }
    }
}
