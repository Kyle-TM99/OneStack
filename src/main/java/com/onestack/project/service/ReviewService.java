package com.onestack.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onestack.project.domain.Review;
import com.onestack.project.mapper.ReviewMapper;

@Service
public class ReviewService {
  

  @Autowired
  private ReviewMapper reviewMapper;

  // 리뷰 작성
  public void createReview(Review review) {
    reviewMapper.createReview(review);
  }

  // 리뷰 조회
  public List<Review> getReviewList(int proNo) {
    return reviewMapper.getReviewList(proNo);
  }
  
}
