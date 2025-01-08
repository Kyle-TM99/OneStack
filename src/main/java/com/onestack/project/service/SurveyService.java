package com.onestack.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onestack.project.domain.SurveyWithCategory;
import com.onestack.project.mapper.SurveyMapper;



@Service
public class SurveyService {
	
	@Autowired
	private SurveyMapper surveyMapper;
	
	public Map<String, Object> getSurvey(int itemNo) {
		
		List<SurveyWithCategory> surveyList = surveyMapper.getSurvey(itemNo);
		
		Map<String, Object> modelMap = new HashMap<>();
		
		modelMap.put("surveyList", surveyList);
	
		return modelMap;
	}

}
