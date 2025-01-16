package com.onestack.project.ajax;

import com.onestack.project.domain.MemProAdInfoCate;
import com.onestack.project.service.ProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ProAjaxController {
    @Autowired
    private ProService proService;

    /* 필터링 조건이 바뀌었을 때 전문가 리스트 반환 */
    @PostMapping("/proFilter")
    public List<MemProAdInfoCate> filterPros(@RequestBody Map<String, Object> requestData) {

        List<String> appType = (List<String>) requestData.get("filters");
        int itemNo = (int) requestData.get("itemNo");

        // 필터 조건에 맞는 전문가 리스트 가져오기
        return proService.getFilteredPros(appType, itemNo);
    }

    /* 필터링 조건과 정렬 조건이 바뀌었을 때 전문가 리스트 반환 */
    @PostMapping("/proFilterSort")
    public List<MemProAdInfoCate> getFilteredAndSortedPros(@RequestBody Map<String, Object> requestData) {

        List<String> appType = (List<String>) requestData.get("filters");
        String sort = (String) requestData.get("sort");
        int itemNo = (int) requestData.get("itemNo");

        return proService.getFilteredAndSortedPros(appType, sort, itemNo);
    }



}
