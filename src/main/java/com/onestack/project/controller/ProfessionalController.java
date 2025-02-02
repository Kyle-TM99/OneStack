package com.onestack.project.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onestack.project.domain.*;
import com.onestack.project.service.ProService;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.onestack.project.service.MemberService;
import com.onestack.project.service.ProfessionalService;
import com.onestack.project.service.SurveyService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class ProfessionalController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProfessionalService professionalService;

    @Autowired
    private ProService proService;

    @Autowired
    private SurveyService surveyService;

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

    /* 견적 요청서 폼 */
    @GetMapping("/estimationForm")
    public String getEstimationForm(Model model, @RequestParam(value = "proNo") int proNo) {

        model.addAttribute("proNo", proNo);

        return "views/estimationForm";
    }

    /* 견적 요청서 작성 */
    @PostMapping("/submitEstimation")
    public String submitEstimation(Estimation estimation, @RequestParam("proNo") int proNo) {

        proService.submitEstimation(estimation);

        return "redirect:/doneEstimation";
    }

    /* 견적 요청 완료 페이지 */
    @GetMapping("/doneEstimation")
    public String estimationDoneForm() {
        return "views/estimationDoneForm";
    }


    /* 전문가 상세보기 */
    @GetMapping("/proDetail")
    public String getProDetail(Model model, @RequestParam(value = "proNo") int proNo) {
        List<MemberWithProfessional> proList = professionalService.getPro2(proNo);

        model.addAttribute("proList", proList);

        return "views/proDetail";
    }


    @GetMapping("/survey")
    public String getSurveyForm(@RequestParam("itemNo") int itemNo, Model model) {
        Map<String, Object> surveyData = surveyService.getSurvey(itemNo);
        model.addAllAttributes(surveyData);
        return "views/surveypage";
    }

    @GetMapping("/proConversion")
    public String getProConversion(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        String memberId = member.getMemberId();

        if (memberId == null) {
            return "redirect:/loginForm";
        }

        model.addAttribute("member", memberService.getMember(memberId));
        model.addAttribute("categories", surveyService.getAllCategories());

        return "views/proConversion";
    }

    @PostMapping("/proConversion/save")
    @ResponseBody
    public ResponseEntity<?> saveProfessionalData(@RequestBody ProConversionRequest request) {
        try {
            log.info("수신된 데이터: {}", request);

            // 빈 Survey Answer 제거
            List<String> filteredAnswers = request.getSurveyAnswers().stream()
                    .filter(answer -> answer != null && !answer.trim().isEmpty())
                    .toList();
            request.setSurveyAnswers(filteredAnswers);

            // 데이터 저장
            professionalService.saveProConversionData(request);

            return ResponseEntity.ok(Collections.singletonMap("message", "전문가 신청이 완료되었습니다."));
        } catch (Exception e) {
            log.error("전문가 데이터 저장 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "저장 실패"));
        }
    }


    @GetMapping("/portfolio")
    public String getPortfolio(@RequestParam(value = "itemNo", required = false, defaultValue = "11") int itemNo, Model model) {
        Map<String, Object> surveyData = surveyService.getSurvey(itemNo);
        model.addAllAttributes(surveyData);
        return "views/portfolioManagement";
    }

    @GetMapping("/portfolioList")
    public String getPortfolioList(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        Integer memberNo = member.getMemberNo();

        // memberNo가 세션에서 가져와지는지 확인하는 디버깅 로그 추가
        if (memberNo == null) {
            return "redirect:/loginForm";
        }

        List<Portfolio> portfolioList = professionalService.getPortfoliosByMember(memberNo);
        System.out.println(" 조회된 포트폴리오 개수: " + portfolioList.size());

        model.addAttribute("portfolioList", portfolioList);

        return "views/portfolioList";
    }

    @GetMapping("/portfolioList/addPortfolio")
    public String showAddPortfolioForm(HttpSession session, Model model,
                                       @RequestParam(value = "itemNo", required = false, defaultValue = "0") int itemNo) {
        // 세션에서 회원 정보 가져오기
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/loginForm";
        }

        // 설문조사 데이터 가져오기
        Map<String, Object> surveyData = surveyService.getSurvey(itemNo);
        model.addAllAttributes(surveyData);

        // 모델 속성 추가
        model.addAttribute("selectedItemNo", itemNo);
        model.addAttribute("member", memberService.getMember(member.getMemberId()));
        model.addAttribute("categories", surveyService.getAllCategories());

        return "views/addPortfolio";
    }

    @GetMapping("/portfolio/portfolioDetail")
    public String getPortfolioDetail(@RequestParam("portfolioNo") int portfolioNo, Model model) {
        PortfolioDetail portfolio = professionalService.getPortfolioById(portfolioNo); // 단일 객체 반환
        model.addAttribute("portfolio", portfolio);
        return "views/portfolioDetail";  // 템플릿 경로 맞추기
    }


//    @GetMapping("/editPortfolio")
//    public String getEditPortfolioPage(
//            @RequestParam("portfolioNo") int portfolioNo,
//            @RequestParam(value = "itemNo", required = false, defaultValue = "0") int itemNo,
//            Model model, HttpSession session) {
//
//        // ✅ 세션에서 회원 정보 가져오기
//        Member member = (Member) session.getAttribute("member");
//        if (member == null) {
//            return "redirect:/loginForm";
//        }
//
//        // ✅ 기존 포트폴리오, 전문가, 전문가 고급정보 가져오기
//        Portfolio portfolio = professionalService.getPortfolioById(portfolioNo);
//        Professional professional = professionalService.getProfessionalByPortfolio(portfolioNo);
//        ProfessionalAdvancedInformation advancedInfo = professionalService.getAdvancedInfoByPortfolio(portfolioNo);
//
//        if (portfolio == null || professional == null || advancedInfo == null) {
//            return "redirect:/errorPage";  // 데이터가 없으면 오류 페이지로 리다이렉트
//        }
//
//        Map<String, Object> surveyData = surveyService.getSurvey(itemNo);
//        model.addAllAttributes(surveyData);
//
//        // ✅ 모델 속성 추가 (HTML에서 사용 가능)
//        model.addAttribute("selectedItemNo", itemNo);
//        model.addAttribute("portfolio", portfolio);
//        model.addAttribute("professional", professional);
//        model.addAttribute("advancedInfo", advancedInfo);
//        model.addAttribute("categories", surveyService.getAllCategories());
//
//        return "views/editPortfolio"; // 📌 HTML 페이지 경로
//    }

//    /**
//     * ✅ 포트폴리오 수정 (업데이트 처리)
//     */
//    @PostMapping("/editPortfolio/submit")
//    public String editPortfolio(
//            @RequestParam("portfolioNo") int portfolioNo,
//            @RequestParam("categoryNo") int categoryNo,
//            @RequestParam("itemNo") int itemNo,
//            @RequestParam("selfIntroduction") String selfIntroduction,
//            @RequestParam("contactableTimeStart") String contactableTimeStart,
//            @RequestParam("contactableTimeEnd") String contactableTimeEnd,
//            @RequestParam("career") String career,
//            @RequestParam("awardCareer") String awardCareer,
//            @RequestParam("portfolioTitle") String portfolioTitle,
//            @RequestParam("portfolioContent") String portfolioContent,
//            @RequestParam(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
//            @RequestParam(value = "portfolioFiles", required = false) MultipartFile[] portfolioFiles,
//            RedirectAttributes redirectAttributes) {
//
//        try {
//            // ✅ 기존 포트폴리오 가져오기
//            Portfolio existingPortfolio = professionalService.getPortfolioById(portfolioNo);
//            if (existingPortfolio == null) {
//                redirectAttributes.addFlashAttribute("error", "포트폴리오를 찾을 수 없습니다.");
//                return "redirect:/portfolioList";
//            }
//
//            // ✅ 기존 전문가 정보 가져오기
//            Professional professional = professionalService.getProfessionalByPortfolio(portfolioNo);
//            ProfessionalAdvancedInformation advancedInfo = professionalService.getAdvancedInfoByPortfolio(portfolioNo);
//
//            // ✅ 포트폴리오 데이터 업데이트
//            existingPortfolio.setPortfolioTitle(portfolioTitle);
//            existingPortfolio.setPortfolioContent(portfolioContent);
//
//            // ✅ 기존 썸네일 유지 or 새로운 썸네일 저장
//            if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
//                existingPortfolio.setThumbnailImage(professionalService.uploadImage(thumbnailImage));
//            }
//
//            // ✅ 기존 포트폴리오 파일 유지 or 새로운 파일 저장
//            List<String> portfolioFileUrls = (portfolioFiles != null && portfolioFiles.length > 0)
//                    ? professionalService.uploadPortfolioFiles(portfolioFiles)
//                    : new ArrayList<>();
//
//            // ✅ 전문가 정보 업데이트
//            professional.setCategoryNo(categoryNo);
//            professional.setSelfIntroduction(selfIntroduction);
//            professional.setCareer(career);
//            professional.setAwardCareer(awardCareer);
//            professional.setContactableTime(contactableTimeStart + " - " + contactableTimeEnd);
//
//            // ✅ 전문가 고급정보 업데이트
//            advancedInfo.setItemNo(itemNo);
//
//            // ✅ 최종 업데이트 실행
//            professionalService.updatePortfolio(existingPortfolio, professional, advancedInfo, portfolioFileUrls);
//
//            redirectAttributes.addFlashAttribute("success", "포트폴리오가 수정되었습니다.");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "포트폴리오 수정 중 오류가 발생했습니다.");
//        }
//
//        return "redirect:/portfolioList";
//    }



    @DeleteMapping("/portfolio/delete")
    @ResponseBody
    public ResponseEntity<?> deletePortfolio(@RequestParam("portfolioNo") int portfolioNo) {
        try {
            // ✅ 포트폴리오 및 연관 데이터 삭제
            professionalService.deletePortfolio(portfolioNo);
            return ResponseEntity.ok(Map.of("message", "포트폴리오 및 관련 데이터가 삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "삭제 실패"));
        }
    }

}
