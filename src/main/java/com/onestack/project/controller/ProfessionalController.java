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

import com.onestack.project.service.SurveyService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ProfessionalController {

	@Autowired
	private SurveyService surveyService;
	

	@GetMapping("/survey")
	public String getSurveyForm(@RequestParam("itemNo") int itemNo, Model model) {
	    Map<String, Object> surveyData = surveyService.getSurvey(itemNo);

	    model.addAllAttributes(surveyData);
	    return "views/surveypage";
	}
	
	@PostMapping("/proConversion/update")
	@ResponseBody
	public Map<String, Object> updatePortfolioAndSurvey(
	        @RequestParam("portfolioTitle") String portfolioTitle,
	        @RequestParam("portfolioContent") String portfolioContent,
	        @RequestParam Map<String, String> surveyAnswers,
	        @RequestParam("thumbnailImage") MultipartFile thumbnailImage,
	        @RequestParam Map<String, MultipartFile> portfolioFiles) {

	    log.info("Received portfolioTitle: {}", portfolioTitle);
	    log.info("Received portfolioContent: {}", portfolioContent);
	    log.info("Received surveyAnswers: {}", surveyAnswers);

	    // 파일 저장 로직
	    String thumbnailImagePath = saveFile(thumbnailImage, "thumbnail");

	    List<String> portfolioFilePaths = portfolioFiles.entrySet().stream()
	            .filter(entry -> entry.getKey().startsWith("portfolioFile") && entry.getValue() != null && !entry.getValue().isEmpty())
	            .map(entry -> saveFile(entry.getValue(), "portfolio"))
	            .collect(Collectors.toList());

	    log.info("Thumbnail Image Path: {}", thumbnailImagePath);
	    log.info("Portfolio Files Paths: {}", portfolioFilePaths);

	    Map<String, Object> responseData = new HashMap<>();
	    responseData.put("success", true);
	    responseData.put("portfolioTitle", portfolioTitle);
	    responseData.put("portfolioContent", portfolioContent);
	    responseData.put("surveyAnswers", surveyAnswers);
	    responseData.put("thumbnailImagePath", thumbnailImagePath);
	    responseData.put("portfolioFiles", portfolioFilePaths);

	    return responseData;
	}

	/**
	 * 파일 저장 로직
	 * @param file 업로드된 파일
	 * @param directory 저장 디렉토리 이름
	 * @return 저장된 파일 경로
	 */
	private String saveFile(MultipartFile file, String directory) {
	    if (file == null || file.isEmpty()) {
	        return null;
	    }
	    try {
	        String uploadDir = "/uploads/" + directory;
	        File dir = new File(uploadDir);
	        if (!dir.exists()) {
	            dir.mkdirs();
	        }

	        String filePath = uploadDir + "/" + file.getOriginalFilename();
	        file.transferTo(new File(filePath));
	        return filePath;
	    } catch (IOException e) {
	        log.error("Failed to save file: {}", file.getOriginalFilename(), e);
	        return null;
	    }
	}


	@GetMapping("/proConversion")
	public String getProConversion(Model model) {
	    model.addAttribute("surveyData", surveyService.getSurvey(1));
	    return "views/proConversion";
	}


}
