package com.onestack.project.controller;

import com.onestack.project.domain.MemProAdInfoCate;
import com.onestack.project.domain.Professional;
import com.onestack.project.service.ProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProAjaxController {
    @Autowired
    private ProService proService;

    /* 필터링 조건과 정렬 조건이 바뀌었을 때 전문가 리스트 반환 및 무한스크롤 */
    @PostMapping("/proFilter")
    public Map<String, Object> filterPros2(@RequestBody Map<String, Object> requestData) {

        List<String> appType = (List<String>) requestData.get("filters");
        String sort = (String) requestData.get("sort");
        int itemNo = (int) requestData.get("itemNo");
        int page = (int) requestData.get("page");
        int size = (int) requestData.get("size");

        // 필터 조건이 없는 경우 기본값 지정
        if (sort == null || sort.isEmpty()) {
            sort = "default";
        }

        // 필터 조건에 맞는 전문가 리스트 가져오기
        List<MemProAdInfoCate> pros = proService.getPaginatedFilteredAndSortedPros(appType, sort, itemNo, page, size);

        // 필터 조건에 맞는 전문가 평균 가격 출력용
        List<MemProAdInfoCate> pros1 = proService.getFilteredAndSortedPros(appType, sort, itemNo);

        // 조건별 전문가들의 평균 가격 계산
        double overallAveragePrice = pros1.stream()
                .map(MemProAdInfoCate::getProfessional) // MemProAdInfoCate 객체에서 Professional 추출
                .mapToDouble(Professional::getAveragePrice) // Professional 객체에서 평균 가격 추출
                .average() // 평균 계산
                .orElse(0.0); // 값이 없으면 기본값 0.0 반환

        String formattedAveragePrice = String.format("%,d", (long) overallAveragePrice);

        Map<String, Object> response = new HashMap<>();
        response.put("pros", pros);
        response.put("overallAveragePrice", formattedAveragePrice);

        return response;
    }

}
