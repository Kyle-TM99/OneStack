package com.onestack.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onestack.project.domain.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onestack.project.domain.Category;
import com.onestack.project.domain.SurveyWithCategory;
import com.onestack.project.mapper.SurveyMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SurveyService {
	
	@Autowired
	private SurveyMapper surveyMapper;
	
	public Map<String, Object> getSurvey(int itemNo) {
	    List<SurveyWithCategory> surveyList = surveyMapper.getSurvey(itemNo);
	    Map<String, Object> modelMap = new HashMap<>();
	    modelMap.put("surveyList", surveyList);
	    return modelMap;
	}
	
	public List<Category> getAllCategories() {
	        return surveyMapper.getAllCategories();
	    }

}

