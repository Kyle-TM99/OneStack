package com.onestack.project.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onestack.project.domain.Portfolio;
import com.onestack.project.domain.ProConversionRequest;
import com.onestack.project.domain.Professional;
import com.onestack.project.domain.ProfessionalAdvancedInformation;
import com.onestack.project.service.MemberService;
import com.onestack.project.service.ProfessionalService;
import com.onestack.project.service.SurveyService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProfessionalController {

	@Autowired
	private SurveyService surveyService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ProfessionalService professionalService;

	@GetMapping("/survey")
	public String getSurveyForm(@RequestParam("itemNo") int itemNo, Model model) {
		Map<String, Object> surveyData = surveyService.getSurvey(itemNo);
		model.addAllAttributes(surveyData);
		return "views/surveypage";
	}

	@PostMapping("/proConversion/update")
	@ResponseBody
	public Map<String, Object> updatePortfolioAndSurvey(@RequestParam("portfolioTitle") String portfolioTitle,
			@RequestParam("portfolioContent") String portfolioContent,
			@RequestParam("surveyAnswers") String surveyAnswersJson,
			@RequestParam("thumbnailImage") MultipartFile thumbnailImage,
			@RequestParam Map<String, MultipartFile> portfolioFiles) throws IOException {

		log.info("Received portfolioTitle: {}", portfolioTitle);
		log.info("Received portfolioContent: {}", portfolioContent);
		log.info("Received surveyAnswers JSON: {}", surveyAnswersJson);
		log.info("썸네일 파일 이름: {}", thumbnailImage != null ? thumbnailImage.getOriginalFilename() : "없음");
		log.info("포트폴리오 파일 개수: {}", portfolioFiles.size());
		portfolioFiles.forEach((key, file) -> log.info("포트폴리오 파일: {} -> {}", key, file.getOriginalFilename()));

		// 설문조사 응답 역직렬화
		ObjectMapper objectMapper = new ObjectMapper();
		List<Map<String, String>> surveyAnswers = objectMapper.readValue(surveyAnswersJson, new TypeReference<>() {
		});
		log.info("Parsed surveyAnswers: {}", surveyAnswers);

		// 파일 저장: 썸네일 이미지
		String thumbnailImagePath = professionalService.saveFile(thumbnailImage, "thumbnail");

		// 포트폴리오 파일에서 썸네일 파일 제외
		Map<String, MultipartFile> filteredPortfolioFiles = portfolioFiles.entrySet().stream()
				.filter(entry -> !entry.getKey().equals("thumbnailImage")) // "thumbnailImage" 키 필터링
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		// 파일 저장: 포트폴리오 이미지
		List<String> portfolioFilePaths = professionalService.saveFiles(filteredPortfolioFiles, "portfolio");

		// 상대 경로로 변환 (클라이언트가 접근 가능하도록 설정)
		thumbnailImagePath = "/uploads/thumbnail/" + new File(thumbnailImagePath).getName();
		portfolioFilePaths = portfolioFilePaths.stream().map(path -> "/uploads/portfolio/" + new File(path).getName())
				.collect(Collectors.toList());

		log.info("Thumbnail Image Path: {}", thumbnailImagePath);
		log.info("Portfolio Files Paths: {}", portfolioFilePaths);

		// 응답 데이터 생성
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("success", true);
		responseData.put("portfolioTitle", portfolioTitle);
		responseData.put("portfolioContent", portfolioContent);
		responseData.put("surveyAnswers", surveyAnswers);
		responseData.put("thumbnailImagePath", thumbnailImagePath);
		responseData.put("portfolioFilePaths", portfolioFilePaths);

		return responseData;
	}

	@PostMapping("/proConversion/submit")
    public String submitProConversion(
            ProConversionRequest request, 
            Model model
			/* @RequestParam("thumbnailImage") MultipartFile thumbnailImage, 
            @RequestParam(value = "portfolioFiles", required = false) List<MultipartFile> portfolioFiles*/) {

    	log.info("Hello, World!!");
    	
        log.info("Received ProConversionRequest: {}", request);

                
        

        // 전문가 정보 저장 로직
        Professional professional = new Professional();
        professional.setMemberNo(request.getMemberNo());
        professional.setItemNo(request.getItemNo());
        professional.setSelfIntroduction(request.getSelfIntroduction());
        professional.setCarrer(String.join(",", request.getCarrer()));
        professional.setAwardCarrer(String.join(",", request.getAwardCarrer()));

        Integer proNo = professionalService.addProfessional(professional);

        // 전문가 고급 정보 저장 로직
        ProfessionalAdvancedInformation proAdvancedInfo = new ProfessionalAdvancedInformation();
        proAdvancedInfo.setProNo(proNo);
        proAdvancedInfo.setItemNo(request.getItemNo());

        List<Map<String, String>> surveyAnswers = request.getProAnswers();
        if (surveyAnswers != null) {
            for (int i = 0; i < surveyAnswers.size() && i < 5; i++) {
                String answer = surveyAnswers.get(i).get("answer");
                switch (i) {
                    case 0 -> proAdvancedInfo.setProAnswer1(answer);
                    case 1 -> proAdvancedInfo.setProAnswer2(answer);
                    case 2 -> proAdvancedInfo.setProAnswer3(answer);
                    case 3 -> proAdvancedInfo.setProAnswer4(answer);
                    case 4 -> proAdvancedInfo.setProAnswer5(answer);
                }
            }
        }

        Integer proAdvancedNo = professionalService.addProfessionalAdvancedInfo(proAdvancedInfo);

        // 포트폴리오 저장 로직
        Portfolio portfolio = new Portfolio();
        portfolio.setProNo(proNo);
        portfolio.setProAdvancedNo(proAdvancedNo);
        portfolio.setPortfolioTitle(request.getPortfolioTitle());
        portfolio.setPortfolioContent(request.getPortfolioContent());        
        professionalService.addPortfolio(portfolio);

        model.addAttribute("success", true);
        return "views/main";
    }

	@GetMapping("/proConversion")
	public String getProConversion(HttpSession session, Model model) {
		String memberId = (String) session.getAttribute("memberId");
		log.info("Session memberId: {}", memberId); // 여기서 null인지 확인

		if (memberId == null || memberId.isEmpty()) {
			return "redirect:/loginForm"; // 로그인 페이지로 리다이렉트
		}

		int memberNo = memberService.getMemberById(memberId); // 여기서 null이 반환되거나 예외 발생 가능
		log.info("Retrieved memberNo: {}", memberNo);

		model.addAttribute("member", memberService.getMember(memberId));
		model.addAttribute("surveyData", surveyService.getSurvey(1));

		return "views/proConversion";
	}
}
