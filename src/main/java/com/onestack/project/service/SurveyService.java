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

	public List<Map<String, Object>> getAllSurveysAsMap() {
		List<Survey> surveys = surveyMapper.getAllSurveys();
		List<Map<String, Object>> surveyList = new ArrayList<>();

		for (Survey survey : surveys) {
			Map<String, Object> surveyMap = new HashMap<>();
			surveyMap.put("surveyNo", survey.getSurveyNo());
			surveyMap.put("itemNo", survey.getItemNo());
			surveyMap.put("surveyQuestion", survey.getSurveyQuestion());
			surveyMap.put("surveyOption", survey.getSurveyOption());
			surveyList.add(surveyMap);
		}

		return surveyList;
	}

	}

