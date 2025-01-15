package com.onestack.project.controller;


import java.util.Map;


import com.onestack.project.service.ProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class ProfessionalController {

	@Autowired
	private ProService proService;

	@GetMapping("/survey")
	public String getSurveyForm(Model model, @RequestParam(value = "itemNo", required = false, defaultValue = "11") int itemNo) {

		Map<String, Object> modelMap = proService.getSurvey(itemNo);

		model.addAllAttributes(modelMap);

		return "views/surveyPage";

	}

	@GetMapping("/findPro")
	public String getProList(Model model, @RequestParam(value = "itemNo", required = false, defaultValue = "11") int itemNo) {

		Map<String, Object> surveyModelMap = proService.getSurvey(itemNo);
		Map<String, Object> proModelMap = proService.getMemProAdCateInfo(itemNo);

		model.addAllAttributes(surveyModelMap);
		model.addAllAttributes(proModelMap);
		model.addAttribute("itemNo", itemNo);

		return "views/findPro";
	}
	
	
}
