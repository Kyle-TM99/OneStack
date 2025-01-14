package com.onestack.project.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    public Map<String, Object> updatePortfolioAndSurvey(
            @RequestParam("portfolioTitle") String portfolioTitle,
            @RequestParam("portfolioContent") String portfolioContent,
            @RequestParam("surveyAnswers") String surveyAnswersJson,
            @RequestParam("thumbnailImage") MultipartFile thumbnailImage,
            @RequestParam Map<String, MultipartFile> portfolioFiles) throws IOException {

        log.info("Received portfolioTitle: {}", portfolioTitle);
        log.info("Received portfolioContent: {}", portfolioContent);
        log.info("Received surveyAnswers JSON: {}", surveyAnswersJson);

        // 설문조사 응답 역직렬화
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, String>> surveyAnswers = objectMapper.readValue(surveyAnswersJson, new TypeReference<>() {});
        log.info("Parsed surveyAnswers: {}", surveyAnswers);

        // 파일 저장
        String thumbnailImagePath = saveFile(thumbnailImage, "thumbnail");

        List<String> portfolioFilePaths = portfolioFiles.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("portfolioFile") && entry.getValue() != null && !entry.getValue().isEmpty())
                .map(entry -> saveFile(entry.getValue(), "portfolio"))
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
        responseData.put("portfolioFiles", portfolioFilePaths);

        return responseData;
    }

    private String saveFile(MultipartFile file, String directory) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String baseDir = System.getProperty("user.dir") + "/uploads/" + directory;
            File dir = new File(baseDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String filePath = baseDir + "/" + uniqueFileName;
            file.transferTo(new File(filePath));
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
        }
    }

    @PostMapping("/proConversion/submit")
    public String submitProConversion(ProConversionRequest request, Model model,
							    		@RequestParam("thumbnailImage") MultipartFile thumbnailImage,
							            @RequestParam Map<String, MultipartFile> portfolioFiles) {
        System.out.println("Received ProConversionRequest: " + request);
        System.out.println("Received Thumbnail: " + thumbnailImage.getOriginalFilename());
        System.out.println("Received Portfolio Files: " + portfolioFiles.keySet());
        System.out.println("ProAnswers: " + request.getProAnswers());
        System.out.println("Received Thumbnail Image: " + thumbnailImage.getOriginalFilename());
        System.out.println("Received Portfolio Files: " + portfolioFiles.keySet());

        // 경력과 수상경력을 쉼표로 구분된 문자열로 변환
        String carrerString = String.join(",", request.getCarrer());
        String awardCarrerString = String.join(",", request.getAwardCarrer());

        // 전문가 기본 정보 저장
        Professional professional = new Professional();
        professional.setMemberNo(request.getMemberNo());
        professional.setItemNo(request.getItemNo());
        professional.setSelfIntroduction(request.getSelfIntroduction());
        professional.setCarrer(carrerString);
        professional.setAwardCarrer(awardCarrerString);

        Integer proNo = professionalService.addProfessional(professional);

        // 전문가 고급 정보 저장
        ProfessionalAdvancedInformation proAdvancedInfo = new ProfessionalAdvancedInformation();
        proAdvancedInfo.setProNo(request.getMemberNo());
        proAdvancedInfo.setItemNo(request.getItemNo());

        List<Map<String, String>> surveyAnswers = request.getProAnswers();
        if (surveyAnswers == null || surveyAnswers.isEmpty() || surveyAnswers.get(0).get("answer") == null) {
            throw new IllegalArgumentException("pro_answer1 is mandatory and cannot be null.");
        }

        proAdvancedInfo.setProAnswer1(surveyAnswers.get(0).get("answer"));
        proAdvancedInfo.setProAnswer2(surveyAnswers.size() > 1 ? surveyAnswers.get(1).get("answer") : null);
        proAdvancedInfo.setProAnswer3(surveyAnswers.size() > 2 ? surveyAnswers.get(2).get("answer") : null);
        proAdvancedInfo.setProAnswer4(surveyAnswers.size() > 3 ? surveyAnswers.get(3).get("answer") : null);
        proAdvancedInfo.setProAnswer5(surveyAnswers.size() > 4 ? surveyAnswers.get(4).get("answer") : null);

        Integer proAdvancedNo = professionalService.addProfessionalAdvancedInfo(proAdvancedInfo);

        // 포트폴리오 저장
        Portfolio portfolio = new Portfolio();
        portfolio.setProNo(proNo);
        portfolio.setProAdvancedNo(proAdvancedNo);
        portfolio.setPortfolioTitle(request.getPortfolioTitle());
        portfolio.setPortfolioContent(request.getPortfolioContent());
        portfolio.setThumbnailImage(request.getThumbnailImage().getOriginalFilename());

        professionalService.savePortfolioFiles(portfolioFiles, portfolio);
        professionalService.addPortfolio(portfolio);

        model.addAttribute("success", true);
        return "views/main";
    }



    @GetMapping("/proConversion")
    public String getProConversion(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");

        if (memberId == null || memberId.isEmpty()) {
            return "redirect:/loginForm"; // 로그인 페이지로 리다이렉트
        }

        int memberNo = memberService.getMemberById(memberId);

        model.addAttribute("member", memberService.getMember(memberId));
        model.addAttribute("surveyData", surveyService.getSurvey(1));

        return "views/proConversion";
    }
}

