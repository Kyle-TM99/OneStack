package com.onestack.project.service;

import com.onestack.project.domain.Estimation;
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
    public Map<String, Object> getFilter(int itemNo) {

        List<SurveyWithCategory> filterList = proMapper.getFilter(itemNo);

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("filterList", filterList);

        return modelMap;
    }

    /* itemNo에 따른 전문가 전체 리스트 반환 */
    public Map<String, Object> getMemProAdCateInfo(int itemNo) {
        List<MemProAdInfoCate> proList = proMapper.getMemProAdCateInfo(itemNo);

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("proList", proList);

        return modelMap;
    }

    /* 필터링 조건과 정렬 조건에 따른 전문가 리스트 반환 */
    public List<MemProAdInfoCate> getFilteredAndSortedPros(List<String> appType, String sort, int itemNo) {
        return proMapper.getFilteredAndSortedPros(appType, sort, itemNo);
    }

    /* 견적 요청서 작성 */
    public void submitEstimation(Estimation estimation) {
        proMapper.submitEstimation(estimation);
    }

    /* 전문가 평점 업데이트 */
    public void updateProRating(int rate, int proNo) {
        proMapper.updateProRating(rate, proNo);
    }


}
