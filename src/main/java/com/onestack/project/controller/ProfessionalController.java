package com.onestack.project.controller;


import java.util.Map;


import com.onestack.project.service.ProService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProfessionalController {

	@Autowired
	private ProService proService;

	/* itemNo에 따른 전문가 전체 리스트 출력 */
	@GetMapping("/findPro")
	public String getProList(Model model, @RequestParam(value = "itemNo") int itemNo) {

		Map<String, Object> surveyModelMap = proService.getSurvey(itemNo);
		Map<String, Object> proModelMap = proService.getMemProAdCateInfo(itemNo);

		model.addAllAttributes(surveyModelMap);
		model.addAllAttributes(proModelMap);
		model.addAttribute("itemNo", itemNo);

		return "views/findPro";
	}
	
	
}
