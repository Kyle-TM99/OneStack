package com.onestack.project.controller;


import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.onestack.project.service.SurveyService;



@Controller
public class ProfessionalController {

	@Autowired
	private SurveyService surveyService;

	@GetMapping("/survey")
	public String getSurveyForm(Model model, @RequestParam(value = "itemNo", required = false, defaultValue = "11") int itemNo) {

		// 설문조사 질문 리스트, 제공 답변 가져오기 - 제공 답변은 split이용
		Map<String, Object> modelMap = surveyService.getSurvey(itemNo);

		model.addAllAttributes(modelMap);

		return "views/surveyPage";

	}


}
