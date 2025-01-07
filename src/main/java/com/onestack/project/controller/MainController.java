package com.onestack.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {
	
	@GetMapping({"/", "/main"})
	public String mainPage(Model model) {
		
		return "views/mainPage";
	}
	
	@GetMapping("/loginForm")
	public String loginForm(Model model) {
		return "member/loginForm";
	}
}
