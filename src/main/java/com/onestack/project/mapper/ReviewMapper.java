package com.onestack.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.onestack.project.domain.Review;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReviewMapper {

  // 리뷰 작성
  void createReview(Review review);

  // 리뷰 조회
  List<Review> getReviewList(int proNo);

  // 메인 페이지 리뷰 조회
  List<Review> getMainReviewList(@Param("num1") int num1, @Param("num2") int num2);

  // 리뷰 수 증가
  public void increaseReviewCount(int proNo);
  
}
