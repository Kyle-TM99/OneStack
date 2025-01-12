package com.onestack.project.controller;


import java.util.Map;  


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.onestack.project.domain.Portfolio;
import com.onestack.project.service.ProfessionalService;
import com.onestack.project.service.SurveyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProfessionalController {

	@Autowired
	private SurveyService surveyService;

	@Autowired
	private ProfessionalService professionalService;

	@GetMapping("/survey")
	public String getSurveyForm(@RequestParam("itemNo") int itemNo, Model model) {
	    Map<String, Object> surveyData = surveyService.getSurvey(itemNo);

	    model.addAllAttributes(surveyData);
	    return "views/surveypage";
	}
	
	@GetMapping("/proConversion")
	public String getProConversion() {
		return "views/proConversion";
	}

	@PostMapping("/portfolio/submit")
	public String submitPortfolio(Portfolio portfolio, Model model) {
	    // 포트폴리오 데이터 처리
	    boolean isSaved = professionalService.addPortfolio(portfolio);
	    if (isSaved) {
	        model.addAttribute("portfolioTitle", portfolio.getPortfolioTitle());
	        model.addAttribute("portfolioContent", portfolio.getPortfolioContent());
	        model.addAttribute("message", "포트폴리오가 성공적으로 제출되었습니다.");
	    } else {
	        model.addAttribute("error", "포트폴리오를 저장하는 데 실패했습니다.");
	    }

	    return "views/proConversion";
	}

}
