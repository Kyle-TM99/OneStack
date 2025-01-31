package com.onestack.project.mapper;

import com.onestack.project.domain.Inquiry;
import com.onestack.project.domain.MemberWithInquiry;
import com.onestack.project.domain.InquiryAnswer;
import com.onestack.project.domain.ManagerWithInquiryAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InquiryMapper {

    // 문의글 목록 조회
    List<MemberWithInquiry> getInquiry(@Param("startRow") int startRow,
                                       @Param("num") int num,
                                       @Param("type") String type,
                                       @Param("keyword") String keyword);

    // 검색 조건에 맞는 전체 문의글 수 조회
    int getInquiryCount(@Param("type") String type, @Param("keyword") String keyword);

    // 문의글 상세보기
    public Inquiry getInquiryDetail(int inquiryNo);

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

}