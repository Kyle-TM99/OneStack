package com.onestack.project.controller;

import com.onestack.project.domain.SurveyWithCategory;
import com.onestack.project.mapper.SurveyMapper;
import com.onestack.project.service.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class surveyAjaxController {

    @Autowired
    private SurveyService surveyService;
    @Autowired
    private SurveyMapper surveyMapper;

    @GetMapping("/getSurvey")
    public ResponseEntity<List<SurveyWithCategory>> getSurvey(@RequestParam("itemNo") int itemNo) {
        log.info("📌 [컨트롤러] getSurvey 요청 수신 - itemNo: {}", itemNo);

        try {
            // DB에서 설문조사 데이터 조회
            List<SurveyWithCategory> surveyList = surveyMapper.getSurvey(itemNo);

            // 조회된 데이터가 없는 경우
            if (surveyList == null || surveyList.isEmpty()) {
                log.warn("⚠️ [컨트롤러] 설문조사 데이터 없음 - itemNo: {}", itemNo);
                return ResponseEntity.noContent().build(); // 204 응답 (데이터 없음)
            }

            log.info("✅ [컨트롤러] 설문조사 데이터 조회 성공 - {}개 항목", surveyList.size());
            return ResponseEntity.ok(surveyList); // JSON 데이터 반환

        } catch (Exception e) {
            log.error("🚨 [컨트롤러] 설문조사 데이터 조회 중 예외 발생: ", e);
            return ResponseEntity.internalServerError().build(); // 500 응답 (서버 오류)
        }
    }
}
