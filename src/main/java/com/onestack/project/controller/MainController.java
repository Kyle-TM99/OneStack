package com.onestack.project.controller;

import com.onestack.project.domain.Review;
import com.onestack.project.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.ArrayList;

@Slf4j
@Controller
public class MainController {

	@Autowired
	private ReviewService reviewService;

	@GetMapping({"/", "/mainPage"})
	public String mainPage(Model model) {
		// 리뷰 리스트 초기화 (null이 되지 않도록)
		List<Review> rList1 = reviewService.getMainReviewList(3, 0);  // 첫 번째 4개
		List<Review> rList2 = reviewService.getMainReviewList(3, 3);  // 다음 4개
		
		// null 체크 후 빈 리스트로 초기화
		if (rList1 == null) rList1 = new ArrayList<>();
		if (rList2 == null) rList2 = new ArrayList<>();
		
		model.addAttribute("rList1", rList1);
		model.addAttribute("rList2", rList2);
		
		return "views/mainPage";
	}
	
	@GetMapping("/loginForm")
	public String loginForm(Model model) {
		return "member/loginForm";
	}
}
