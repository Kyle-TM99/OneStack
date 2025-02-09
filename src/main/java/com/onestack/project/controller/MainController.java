package com.onestack.project.controller;

import com.onestack.project.domain.Community;
import com.onestack.project.domain.Review;
import com.onestack.project.mapper.CommunityMapper;
import com.onestack.project.service.CommunityService;
import com.onestack.project.service.MemberService;
import com.onestack.project.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Controller
public class MainController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CommunityMapper communityMapper;

	@Autowired
	private  CommunityService communityService;

	@GetMapping({"/", "/mainPage", "/main"})
	public String mainPage(Model model) {
		// 리뷰 리스트 초기화 (null이 되지 않도록)
		List<Review> rList1 = reviewService.getMainReviewList(3, 0);  // 첫 번째 4개
		List<Review> rList2 = reviewService.getMainReviewList(3, 3);  // 다음 4개

		Map<String, Object> cMap = new HashMap<>();

		cMap.put("startRow", 0);
		cMap.put("type", null);
		cMap.put("keyword", null);
		cMap.put("num", 5);
		cMap.put("order", "recommend");

		List<Community> cList = communityMapper.communityList(cMap);

		int proCount = memberService.getProCount();
		int memberCount = memberService.getMemberCount();
		int estimationCount = memberService.getMainEstimationCount();

		log.info(cList.toString());

		// null 체크 후 빈 리스트로 초기화
		if (rList1 == null) rList1 = new ArrayList<>();
		if (rList2 == null) rList2 = new ArrayList<>();

		// 내용 처리
		cList.forEach(community -> {
			String content = community.getCommunityBoardContent();
			// HTML 태그 제거 및 첫 줄만 추출
			content = content.replaceAll("<[^>]*>", "")
					.split("\n")[0];
			community.setCommunityBoardContent(content);
		});

		model.addAttribute("rList1", rList1);
		model.addAttribute("rList2", rList2);
		model.addAttribute("proCount", proCount);
		model.addAttribute("memberCount", memberCount);
		model.addAttribute("estimationCount", estimationCount);
		model.addAttribute("cList", cList);


		return "views/mainPage";
	}

	@GetMapping({"/loginForm", "/login"})
	public String loginForm(Model model) {
		return "member/loginForm";
	}
}
