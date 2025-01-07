package com.onestack.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfessionalController {
	
	@GetMapping("/survey")
	public String getMethodName() {
		return "views/surveyPage";
	}
	

}
