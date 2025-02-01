package com.onestack.project.mapper;

import java.util.List;

import com.onestack.project.domain.Survey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.onestack.project.domain.Category;
import com.onestack.project.domain.SurveyWithCategory;

@Mapper
public interface SurveyMapper {	
	
	// 설문조사 질문, 제공답변 
	public List<SurveyWithCategory> getSurvey(@Param("itemNo") int itemNo);
	
	List<Category> getAllCategories();

	List<Survey> getAllSurveys();
}
