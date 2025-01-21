package com.onestack.project.mapper;

import com.onestack.project.domain.Inquiry;
import com.onestack.project.domain.MemberWithInquiry;
import com.onestack.project.domain.InquiryAnswer;
import com.onestack.project.domain.ManagerWithInquiryAnswer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InquiryMapper {

    // 문의글 목록 조회 (검색 조건, 페이징 포함)
    List<MemberWithInquiry> getInquiry(Map<String, Object> params);

    // 문의글 개수 조회
    int getInquiryCount(Map<String, Object> params);

    // 문의글 작성
    void addInquiry(Inquiry inquiry);

    // 문의글 수정
    void updateInquiry(Inquiry inquiry);

    // 문의글 삭제
    void deleteInquiry(int inquiryNo);

    // 이전 문의글 번호 조회
    Integer preInquiry(int currentInquiryNo);

    // 다음 문의글 번호 조회
    Integer nextInquiry(int currentInquiryNo);

    // 문의글 만족/불만족 상태 변경
    void updateInquirySatisfaction(Map<String, Object> params);

    // 문의 답변 조회
    List<ManagerWithInquiryAnswer> getInquiryAnswer(int inquiryNo);

    // 문의 답변 추가
    void addInquiryAnswer(InquiryAnswer inquiryAnswer);

    // 문의 답변 수정
    void updateInquiryAnswer(InquiryAnswer inquiryAnswer);

    // 문의 답변 삭제
    void deleteInquiryAnswer(int inquiryAnswerNo);
}