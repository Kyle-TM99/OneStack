package com.onestack.project.mapper;

import com.onestack.project.domain.Inquiry;
import com.onestack.project.domain.MemberWithInquiry;
import com.onestack.project.domain.InquiryAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InquiryMapper {

    // 문의글 목록 조회 (검색 조건, 페이징 포함)
    List<MemberWithInquiry> getInquiry(Map<String, Object> params);

    // 검색 조건에 맞는 전체 문의글 수 조회
    int getInquiryCount(@Param("type") String type, @Param("keyword") String keyword);

    // 문의글 상세보기
    Inquiry getInquiryDetail(int inquiryNo);

    // 문의글 작성
    void addInquiry(Inquiry inquiry);

    // 문의글 수정
    void updateInquiry(Inquiry inquiry);

    // 문의글 삭제
    void deleteInquiry(int inquiryNo);

    // 문의글 만족/불만족 상태 변경
    void updateInquirySatisfaction(Map<String, Object> params);

    // 문의 답변 조회
    List<InquiryAnswer> getInquiryAnswer(@Param("inquiryNo") int inquiryNo);

    // 문의 답변 추가
    void addInquiryAnswer(InquiryAnswer inquiryAnswer);

    // 문의 답변 수정
    void updateInquiryAnswer(InquiryAnswer inquiryAnswer);

    // 문의 답변 삭제
    void deleteInquiryAnswer(int inquiryAnswerNo);

    // 문의글 상태를 '답변 중'으로 업데이트
    void updateInquiryStatusToInProgress(int inquiryNo);

    // 문의글 상태를 '답변 완료'로 업데이트
    void updateInquiryStatusToCompleted(int inquiryNo);

}