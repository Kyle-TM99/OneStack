package com.onestack.project.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.onestack.project.domain.Review;

@Mapper
public interface ReviewMapper {

  // 리뷰 작성
  void createReview(Review review);
  
}
