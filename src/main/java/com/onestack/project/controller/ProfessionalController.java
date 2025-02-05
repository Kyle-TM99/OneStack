package com.onestack.project.controller;

import java.util.*;

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
import com.onestack.project.service.ReviewService;
import com.onestack.project.service.SurveyService;

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

    @Autowired
    private ReviewService reviewService;

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
		try {
			// 견적 정보 설정
			estimation.setProNo(proNo);
			estimation.setProgress(0); // 초기 상태 (요청)

			// 견적 저장
			proService.submitEstimation(estimation);

			return "redirect:/estimationDoneForm";
		} catch (Exception e) {
			log.error("견적 요청 중 오류 발생", e);
			return "redirect:/error";
		}
	}

    /* 견적 요청 완료 페이지 */
    @GetMapping("/estimationDoneForm")
    public String estimationDoneForm() {
        return "views/estimationDoneForm";
    }


    /* 전문가 상세보기 */
    @GetMapping("/proDetail")
    public String getProDetail(Model model, @RequestParam(value = "proNo") int proNo) {
        List<MemberWithProfessional> proList = professionalService.getPro2(proNo);
        List<Review> reviewList = reviewService.getReviewList(proNo);
        List<Integer> itemNoList = professionalService.getItemNo(proNo);

        Map<Integer, String> itemNames = new HashMap<>();
        itemNames.put(11, "기획");
        itemNames.put(12, "웹 개발");
        itemNames.put(13, "소프트웨어 개발");
        itemNames.put(14, "안드로이드 개발");
        itemNames.put(15, "iOS 개발");
        itemNames.put(16, "게임 개발");
        itemNames.put(17, "AI 개발");
        itemNames.put(18, "QA 및 테스트");
        itemNames.put(21, "가공 및 라벨링");
        itemNames.put(22, "데이터 복구");
        itemNames.put(23, "크롤링");
        itemNames.put(24, "DB 구축");
        itemNames.put(25, "통계분석");

        List<String> itemNamesList = new ArrayList<>();
        if (itemNoList != null && !itemNoList.isEmpty()) {
            for (Integer itemNo : itemNoList) {
                String name = itemNames.getOrDefault(itemNo, "기타");
                itemNamesList.add(name);
            }
        } else {
            itemNamesList.add("없음");
        }

        int sum = 0;
        int cnt = 0;
        if(reviewList != null && reviewList.size() > 0) {
            for(Review review : reviewList) {
                sum += review.getReviewRate();
                cnt++;
            }
            sum /= cnt;
        }

        model.addAttribute("proList", proList);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("reviewRateAvg", sum);
        model.addAttribute("itemNamesList", itemNamesList);

        return "views/proDetail";
    }


    /* 설문조사 페이지 */
    @GetMapping("/survey")
    public String getSurveyForm(@RequestParam("itemNo") int itemNo, Model model) {
        Map<String, Object> surveyData = surveyService.getSurvey(itemNo);
        model.addAllAttributes(surveyData);
        return "views/surveypage";
    }

    /* 전문가 정보 저장 */
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

    /* 전문가 정보 저장 */
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

    // 포트폴리오 리스트 조회
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

    // 포트폴리오 추가
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

    // 포트폴리오 상세보기
    @GetMapping("/portfolio/portfolioDetail")
    public String getPortfolioDetail(@RequestParam("portfolioNo") int portfolioNo, Model model, HttpSession session) {
        // 포트폴리오 상세 정보 가져오기
        MemProPortPaiCate portfolioDetail = professionalService.getPortfolioDetail(portfolioNo);

        // 세션에서 로그인한 회원 정보 가져오기
        Member sessionMember = (Member) session.getAttribute("member");
        model.addAttribute("sessionMember", sessionMember);

        // 모델에 데이터 추가
        model.addAttribute("portfolio", portfolioDetail);

        return "views/portfolioDetail"; // Thymeleaf 뷰 반환
    }

    // 포트폴리오 수정
    @GetMapping("/editPortfolio")
    public String getEditPortfolioPage(
            @RequestParam("portfolioNo") int portfolioNo,
            Model model, HttpSession session) {


        log.info("📌 [editPortfolio] 요청 수신 - portfolioNo: {}", portfolioNo);

        // ✅ 세션에서 회원 정보 가져오기
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            log.warn("🚨 세션에 로그인된 회원이 없습니다. 로그인 페이지로 리다이렉트");
            return "redirect:/loginForm";
        }
        log.info("✅ 로그인된 회원 - memberNo: {}", member.getMemberNo());

        // ✅ 기존 포트폴리오, 전문가, 전문가 고급정보 가져오기
        Portfolio portfolio = professionalService.getPortfolioById(portfolioNo);
        Professional professional = professionalService.getProfessionalByPortfolio(portfolioNo);
        ProfessionalAdvancedInformation advancedInfo = professionalService.getAdvancedInfoByPortfolio(portfolioNo);

        if (portfolio == null || professional == null || advancedInfo == null) {
            log.error("🚨 데이터 로드 실패 - portfolio: {}, professional: {}, advancedInfo: {}",
                    portfolio, professional, advancedInfo);
            return "redirect:/errorPage";  // 데이터가 없으면 오류 페이지로 이동
        }

        log.info("✅ 포트폴리오 데이터 로드 성공 - portfolioTitle: {}", portfolio.getPortfolioTitle());
        log.info("✅ 전문가 정보 로드 성공 - proNo: {}", professional.getProNo());

        int categoryNo = professional.getCategoryNo();
        int itemNo = advancedInfo.getItemNo();

        log.info("✅ 저장된 카테고리 번호: {}, 전문분야 번호: {}", categoryNo, itemNo);

        Map<String, Object> surveyData = surveyService.getSurvey(itemNo);
        log.info("✅ 설문조사 데이터 로드 완료 - 항목 개수: {}", surveyData.size());



        // ✅ 연락 가능 시간 파싱
        String contactableTime = professional.getContactableTime(); // 예: "오전 9시~오후 6시"
        String contactableTimeStart = "";
        String contactableTimeEnd = "";

        if (contactableTime != null && contactableTime.contains("-")) {
            String[] timeParts = contactableTime.split("-");
            contactableTimeStart = timeParts.length > 0 ? timeParts[0].trim() : "";
            contactableTimeEnd = timeParts.length > 1 ? timeParts[1].trim() : "";
            log.info("✅ 연락 가능 시간 로드 성공 - 시작: {}, 종료: {}", contactableTimeStart, contactableTimeEnd);
        } else {
            log.warn("⚠️ 연락 가능 시간이 설정되지 않았습니다. (DB 값: {})", contactableTime);
        }

        // ✅ 포트폴리오 파일 리스트 가져오기
        List<String> portfolioFiles = professionalService.getPortfolioFiles(portfolioNo);
        log.info("✅ 저장된 포트폴리오 파일 개수: {}", portfolioFiles.size());

        // ✅ 모델 속성 추가 (Thymeleaf에서 사용 가능)
        model.addAttribute("selectedCategoryNo", categoryNo);
        model.addAttribute("portfolio", portfolio);
        model.addAttribute("professional", professional);
        model.addAttribute("advancedInfo", advancedInfo);
        model.addAttribute("portfolioFiles", portfolioFiles);
        model.addAllAttributes(surveyData);
        model.addAttribute("selectedItemNo", itemNo);
        model.addAttribute("categories", surveyService.getAllCategories());
        model.addAttribute("contactableTimeStart", contactableTimeStart);
        model.addAttribute("contactableTimeEnd", contactableTimeEnd);

        log.info("🎯 [editPortfolio] 데이터 로딩 완료. 페이지 반환.");
        return "views/editPortfolio";
    }

    /**
     * ✅ 포트폴리오 수정 (업데이트 처리)
     */
    @PostMapping("/portfolio/update")
    @ResponseBody
    public ResponseEntity<?> updateProfessionalData(@RequestBody ProUpdateRequest request) {
        try {
            log.info("📩 [updatePortfolio] 요청 수신: {}", request);

            // 빈 Survey Answer 제거
            List<String> filteredAnswers = request.getSurveyAnswers().stream()
                    .filter(answer -> answer != null && !answer.trim().isEmpty())
                    .toList();
            request.setSurveyAnswers(filteredAnswers);

            log.info("✅ 정리된 Survey Answers: {}", filteredAnswers);

            // 데이터 업데이트
            professionalService.updateProConversionData(request);

            log.info("✅ 포트폴리오 업데이트 완료 - portfolioNo: {}", request.getPortfolioNo());
            return ResponseEntity.ok(Collections.singletonMap("message", "포트폴리오가 성공적으로 업데이트되었습니다."));
        } catch (Exception e) {
            log.error("🚨 포트폴리오 업데이트 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "업데이트 실패"));
        }
    }

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
