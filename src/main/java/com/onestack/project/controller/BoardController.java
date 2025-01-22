package com.onestack.project.controller;

import com.onestack.project.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
public class BoardController {

	@Autowired
	public ImageService imageservice;

	@GetMapping("/community")
	public String loginForm(Model model) {
		return "board/community";
	}

	@GetMapping("/communityWriteForm")
	public String joinForm(Model model) {
		return "board/communityWriteForm";
	}

	@PostMapping("/imageTest")
	public String image(Model model, @RequestParam(value = "fileData", required = false) MultipartFile image) throws IOException {
		log.info(imageservice.uploadImage(image));

		return "/loginForm";
	}

}
