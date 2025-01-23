package com.onestack.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardController {

	@GetMapping("/community")
	public String loginForm(Model model) {
		return "board/community";
	}

	@GetMapping("/communityWriteForm")
	public String joinForm(Model model) {
		return "board/communityWriteForm";
	}

}
