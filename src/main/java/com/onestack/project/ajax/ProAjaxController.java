package com.onestack.project.ajax;

import com.onestack.project.domain.MemProAdInfoCate;
import com.onestack.project.service.ProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ProAjaxController {
    @Autowired
    private ProService proService;

    @PostMapping("/proFilter")
    public List<MemProAdInfoCate> filterExperts(@RequestBody Map<String, Object> filters, @RequestParam(value = "itemNo", required = false, defaultValue = "11") int itemNo) {
        List<String> appType = (List<String>) filters.get("appType");

        // 필터 조건에 맞는 전문가 리스트 가져오기
        return proService.getFilteredPros(appType, itemNo);
    }


}
