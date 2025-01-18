package com.onestack.project.controller;


import java.util.List;
import java.util.Map;


import com.onestack.project.domain.Estimation;
import com.onestack.project.domain.MemProAdInfoCate;
import com.onestack.project.domain.Professional;
import com.onestack.project.service.ProService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ProfessionalController {

	@Autowired
	private ProService proService;

	/* itemNo에 따른 필터링, 전문가 전체 리스트 출력 */
	@GetMapping("/findPro")
	public String getProList(Model model, @RequestParam(value = "itemNo") int itemNo) {

		Map<String, Object> surveyModelMap = proService.getFilter(itemNo);
		Map<String, Object> proModelMap = proService.getMemProAdCateInfo(itemNo);

		List<MemProAdInfoCate> proList = (List<MemProAdInfoCate>) proModelMap.get("proList");

		double overallAveragePrice = proList.stream()  // proList에서 스트림 처리
				.map(MemProAdInfoCate::getProfessional)  // MemProAdInfoCate에서 Professional 객체 추출
				.mapToDouble(Professional::getAveragePrice)  // Professional 객체에서 평균 가격 추출
				.average()  // 평균 계산
				.orElse(0.0);

		String formattedAveragePrice = String.format("%,d", (long) overallAveragePrice);

		model.addAllAttributes(surveyModelMap);
		model.addAllAttributes(proModelMap);
		model.addAttribute("itemNo", itemNo);
		model.addAttribute("overallAveragePrice", formattedAveragePrice);

		return "views/findPro";
	}

	@GetMapping("/estimationForm")
	public String getEstimationForm(Model model, @RequestParam(value = "proNo") int proNo) {

		model.addAttribute("proNo", proNo);

		return "views/estimationForm";
	}
	
	@PostMapping("/submitEstimation")
	public String submitEstimation(Estimation estimation) {

		proService.submitEstimation(estimation);

		return "redirect:proDetail";
	}


}
