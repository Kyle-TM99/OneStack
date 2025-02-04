package com.onestack.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.onestack.project.domain.Review;

@Mapper
public interface ReviewMapper {

  // 리뷰 작성
  void createReview(Review review);

  // 리뷰 조회
  List<Review> getReviewList(int proNo);

  // 메인 페이지 리뷰 조회
  List<Review> getMainReviewList(int num1, int num2);
  
}
