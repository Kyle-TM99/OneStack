package com.onestack.project.service;

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

    public Map<String, Object> getSurvey(int itemNo) {

        List<SurveyWithCategory> surveyList = proMapper.getSurvey(itemNo);

        Map<String, Object> modelMap = new HashMap<>();

        modelMap.put("surveyList", surveyList);

        return modelMap;
    }

}
