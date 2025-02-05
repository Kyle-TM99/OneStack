package com.onestack.project.service;

import com.onestack.project.domain.Inquiry;
import com.onestack.project.domain.InquiryAnswer;
import com.onestack.project.domain.Member;
import com.onestack.project.domain.MemberWithInquiry;
import com.onestack.project.mapper.InquiryMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class InquiryService {

    @Autowired
    private InquiryMapper inquiryMapper;

    // 문의글 목록 조회 (검색 조건)
    public List<MemberWithInquiry> getInquiry(int memberNo, String type, String keyword, boolean isAdmin) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberNo", memberNo);
        params.put("isAdmin", isAdmin);

        // 검색 조건이 있는 경우에만 파라미터 추가
        if (type != null && keyword != null && !type.isEmpty() && !keyword.isEmpty()) {
            params.put("type", type);
            params.put("keyword", keyword);
        }

        return inquiryMapper.getInquiry(params);
    }

    // 검색 조건에 맞는 전체 문의글 수 조회
    public int getInquiryCount(String type, String keyword) {
        return inquiryMapper.getInquiryCount(type, keyword);
    }

    // 문의글 작성
    public void addInquiry(Inquiry inquiry, MultipartFile file) throws IOException {
        String uploadDir = "C:/OneStack/OneStack/src/main/resources/static/files/"; // 절대 경로
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs(); // 경로가 없으면 생성
        }

        // 파일 저장
        String fileName = file.getOriginalFilename();
        File destinationFile = new File(uploadPath, fileName);
        file.transferTo(destinationFile); // 파일 저장

        // 파일명 설정
        inquiry.setInquiryFile(fileName);

        // 문의글 저장
        inquiryMapper.addInquiry(inquiry);
    }

    // 파일 없는 문의글 작성
    public void addInquiry(Inquiry inquiry) {
        inquiryMapper.addInquiry(inquiry);
    }

    // 문의글 상세보기
    public Inquiry getInquiryDetail(int inquiryNo) {
        return inquiryMapper.getInquiryDetail(inquiryNo);
    }
    // 문의글 삭제
    public void deleteInquiry(int inquiryNo) {
        inquiryMapper.deleteInquiry(inquiryNo);
    }

    // 문의글 수정
    public void updateInquiry(Inquiry inquiry) {
        inquiryMapper.updateInquiry(inquiry);
    }

    // 문의 답변 조회
    public List<InquiryAnswer> getInquiryAnswer(int inquiryNo) {
        List<InquiryAnswer> answers = inquiryMapper.getInquiryAnswer(inquiryNo);
        // 답변을 등록일 기준으로 내림차순 정렬
        answers.sort(Comparator.comparing(InquiryAnswer::getInquiryAnswerRegDate).reversed());
        return answers;
    }

    // 문의 답변 추가
    public void addInquiryAnswer(InquiryAnswer inquiryAnswer, boolean isAdmin) {
        inquiryMapper.addInquiryAnswer(inquiryAnswer);

        // 관리자일 경우 상태 업데이트
        if (isAdmin) {
            inquiryMapper.updateInquiryStatusToInProgress(inquiryAnswer.getInquiryNo());
        }
    }

    // 문의글 만족/불만족 업데이트 메서드
    public void updateInquirySatisfaction(int inquiryNo, boolean isSatisfied) {
        int status = isSatisfied ? 1 : 0; // 1: 만족, 0: 불만족
        Map<String, Object> params = new HashMap<>();
        params.put("inquiryNo", inquiryNo);
        params.put("status", status);
        inquiryMapper.updateInquirySatisfaction(params);
    }

    // 문의글 답변 완료
    public void completeInquiryAnswer(int inquiryNo) {
        inquiryMapper.updateInquiryStatusToCompleted(inquiryNo); // 상태를 '답변 완료'로 업데이트
    }

    public void handleSatisfaction(int inquiryNo, boolean isSatisfied) {
        Map<String, Object> params = new HashMap<>();
        params.put("inquiryNo", inquiryNo);
        params.put("status", isSatisfied ? 1 : 0); // 1: 만족, 0: 불만족
        inquiryMapper.updateInquirySatisfaction(params); // 맵으로 전달
        inquiryMapper.updateInquiryStatusToCompleted(inquiryNo); // 상태를 '답변 완료'로 업데이트
    }

    public void updateInquiryAnswer(InquiryAnswer inquiryAnswer) {
        inquiryMapper.updateInquiryAnswer(inquiryAnswer);
    }

}