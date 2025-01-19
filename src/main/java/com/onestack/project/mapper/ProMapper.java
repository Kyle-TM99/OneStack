package com.onestack.project.mapper;

import com.onestack.project.domain.Estimation;
import com.onestack.project.domain.MemProAdInfoCate;
import com.onestack.project.domain.SurveyWithCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProMapper {
    /* itemNo에 따른 필터링 반환 */
    public List<SurveyWithCategory> getFilter(@Param("itemNo") int itemNo);

    /* itemNo에 따른 전문가 전체 리스트 반환 */
    public List<MemProAdInfoCate> getMemProAdCateInfo(@Param("itemNo") int itemNo);

    /* 필터링 조건과 정렬 조건에 따른 전문가 리스트 반환 */
    public List<MemProAdInfoCate> getFilteredAndSortedPros(@Param("appType") List<String> appType, @Param("sort") String sort, @Param("itemNo") int itemNo);

    /* 견적 요청서 작성 */
    public void submitEstimation(Estimation estimation);
}
