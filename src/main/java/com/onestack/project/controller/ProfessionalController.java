package com.onestack.project.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.onestack.project.domain.*;
import com.onestack.project.service.ProService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.onestack.project.service.MemberService;
import com.onestack.project.service.ProfessionalService;
import com.onestack.project.service.SurveyService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

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
    public String showAddPortfolioForm(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        String memberId = member.getMemberId();

        if (memberId == null) {
            return "redirect:/loginForm";
        }

        model.addAttribute("member", memberService.getMember(memberId));
        model.addAttribute("categories", surveyService.getAllCategories());
        return "views/addPortfolio";
    }


    @PostMapping("/portfolio/submit")
    @ResponseBody
    public ResponseEntity<?> submitPortfolio(
            @RequestBody ProConversionRequest request,
            HttpSession session) {
        try {
            Member member = (Member) session.getAttribute("member");
            Integer memberNo = member != null ? member.getMemberNo() : null;

            if (memberNo == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
            }

            log.info("포트폴리오 추가 요청 데이터: {}", request);

            // 빈 Survey Answer 제거
            List<String> filteredAnswers = request.getSurveyAnswers().stream()
                    .filter(answer -> answer != null && !answer.trim().isEmpty())
                    .toList();
            request.setSurveyAnswers(filteredAnswers);

            // 사용자 ID 설정
            request.setMemberNo(memberNo);

            // 데이터 저장 (기존 전문가 신청 로직 사용)
            professionalService.saveProConversionData(request);

            return ResponseEntity.ok(Collections.singletonMap("message", "포트폴리오가 성공적으로 추가되었습니다."));
        } catch (Exception e) {
            log.error("포트폴리오 저장 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "포트폴리오 저장 실패"));
        }
    }

}
