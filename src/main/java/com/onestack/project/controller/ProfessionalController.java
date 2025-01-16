package com.onestack.project.controller;


import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onestack.project.domain.ProConversionRequest;
import com.onestack.project.service.MemberService;
import com.onestack.project.service.ProfessionalService;
import com.onestack.project.service.SurveyService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProfessionalController {

	@Autowired
	private SurveyService surveyService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ProfessionalService professionalService;

	@GetMapping("/survey")
	public String getSurveyForm(@RequestParam("itemNo") int itemNo, Model model) {
		Map<String, Object> surveyData = surveyService.getSurvey(itemNo);
		model.addAllAttributes(surveyData);
		return "views/surveypage";
	}

	@GetMapping("/proConversion")
	public String getProConversion(HttpSession session, Model model) {
	    String memberId = (String) session.getAttribute("memberId");
	    if (memberId == null) {
	        return "redirect:/login";
	    }
	    int memberNo = memberService.getMemberById(memberId);
	    model.addAttribute("member", memberService.getMember(memberId));
	    model.addAttribute("categories", surveyService.getAllCategories());

	    return "views/proConversion";
	}

	
	@PostMapping("/proConversion/save")
	@ResponseBody
	public ResponseEntity<?> saveProfessionalData(@RequestBody ProConversionRequest request) {
	    try {
	        log.info("수신된 데이터: {}", request);

	        // 빈 Survey Answer 제거
	        List<String> filteredAnswers = request.getSurveyAnswers().stream()
	                .filter(answer -> answer != null && !answer.trim().isEmpty())
	                .toList();
	        request.setSurveyAnswers(filteredAnswers);

	        // 데이터 저장
	        professionalService.saveProConversionData(request);
	        return ResponseEntity.ok(Collections.singletonMap("message", "전문가 신청이 완료되었습니다."));
	    } catch (Exception e) {
	        log.error("전문가 데이터 저장 실패", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Map.of("message", "저장 실패"));
	    }
	}

}
