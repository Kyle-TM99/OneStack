package com.onestack.project.controller;

import java.util.List;
import java.util.Map;


import com.onestack.project.domain.Reports;
import com.onestack.project.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import com.onestack.project.domain.MemProPortPaiCate;
import com.onestack.project.domain.Member;
import com.onestack.project.service.AdminService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Slf4j
public class AdminController {
	@Autowired
	private AdminService adminService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/delete")
    public String delete(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        boolean isAdmin = member.isAdmin();
        model.addAttribute("member",member);
        return "views/deleteButton";
    }

    @GetMapping("/adminPage")
    public String adminPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Member member = (Member) session.getAttribute("member");

        if (member == null || member.getMemberId() == null) {
            return "redirect:/loginForm";
        }
        if (!member.isAdmin()) {
            return "redirect:/loginForm";
        }
        model.addAttribute("member", memberService.getMember(member.getMemberId()));
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
    public ResponseEntity<Map<String, String>> updateMember(@RequestBody Member request) {
        try {
            adminService.updateMember(request);
            return ResponseEntity.ok(Map.of("message", "회원 정보 수정 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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
        List<Member> members = adminService.getWithdrawalMembers();
        System.out.println("컨트롤러에서 받은 탈퇴 회원 수: " + members.size());
        model.addAttribute("members", members);
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
            String screeningMsg = request.get("screeningMsg") != null ? request.get("screeningMsg").toString() : "";

            // 전문가 심사 상태 업데이트
            adminService.updateProStatus(proNo, professorStatus, screeningMsg);

            return ResponseEntity.ok("심사 승인이 완료되었습니다.");
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

        // 전문가 승인/거부 상태 업데이트
        adminService.updateProStatus(proNo, professorStatus, screeningMsg);

        // 만약 거부(professorStatus == 0)일 경우 전문가 데이터 삭제
        if (professorStatus == 0) {
            adminService.rejectAndDeleteProfessional(proNo);
        }

        return ResponseEntity.ok("심사 수정이 완료되었습니다.");
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
    public String getReviewCompletedInquiry(Model model) {
        List<MemProPortPaiCate> pList = adminService.getMemProPortPaiCate();
        model.addAttribute("pro", pList);
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
    public String getReportProcessingDetails(Model model) {
        List<Reports> reportsList = adminService.getReports();
        System.out.println("reportsList: " + reportsList); // 데이터를 출력해 실제로 값이 있는지 확인

        model.addAttribute("reportsList", reportsList);
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
            @RequestParam("targetId") int targetId,
            @RequestParam("reportsNo")int reportsNo) {

        log.info("테스트: " + type + "a: " +  targetId + "b: " + reportsNo);

        try {
            adminService.disableTarget(type, targetId, reportsNo);
            return ResponseEntity.ok("비활성화 처리 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("비활성화 처리 중 오류 발생: " + e.getMessage());
        }
    }
}
