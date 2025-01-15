package com.onestack.project.mapper;

import com.onestack.project.domain.SurveyWithCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProMapper {

    public List<SurveyWithCategory> getSurvey(@Param("itemNo") int itemNo);

    public List<SurveyWithCategory> getMemProAdCateInfo(@Param("itemNo") int itemNo);

}
