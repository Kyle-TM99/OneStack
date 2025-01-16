package com.onestack.project.service;

import com.onestack.project.domain.MemProAdInfoCate;
import com.onestack.project.domain.SurveyWithCategory;
import com.onestack.project.mapper.ProMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProService {
    private final ProMapper proMapper;

    /* 필터링 가져오기 */
    public Map<String, Object> getSurvey(int itemNo) {

        List<SurveyWithCategory> surveyList = proMapper.getSurvey(itemNo);

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("surveyList", surveyList);

        return modelMap;
    }

    /* itemNo에 따른 전문가 전체 리스트 반환 */
    public Map<String, Object> getMemProAdCateInfo(int itemNo) {
        List<MemProAdInfoCate> proList = proMapper.getMemProAdCateInfo(itemNo);

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("proList", proList);

        return modelMap;
    }

    /* 필터링 조건에 따른 전문가 리스트 반환  */
    public List<MemProAdInfoCate> getFilteredPros(List<String> appType, int itemNo) {
        return proMapper.getFilteredPros(appType, itemNo);
    }

    /* 필터링 조건과 정렬 조건에 따른 전문가 리스트 반환 */
    public List<MemProAdInfoCate> getFilteredAndSortedPros(List<String> appType, String sort, int itemNo) {
        return proMapper.getFilteredAndSortedPros(appType, sort, itemNo);
    }


}
