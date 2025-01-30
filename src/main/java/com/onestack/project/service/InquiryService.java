package com.onestack.project.service;

import com.onestack.project.domain.Inquiry;
import com.onestack.project.domain.InquiryAnswer;
import com.onestack.project.domain.MemberWithInquiry;
import com.onestack.project.mapper.InquiryMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class InquiryService {

    @Autowired
    private InquiryMapper inquiryMapper;

    private static final String FILE_STORAGE_PATH = "src/main/resources/static/files/";

    // 문의글 목록 조회 (검색 조건, 페이징 포함)
    public List<MemberWithInquiry> getInquiry(int startRow, int num, String type, String keyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("startRow", startRow);
        params.put("num", num);

        // 검색 조건이 있는 경우에만 파라미터 추가
        if (type != null && keyword != null && !type.isEmpty() && !keyword.isEmpty()) {
            params.put("type", type);
            params.put("keyword", keyword);
        }

        return inquiryMapper.getInquiry(params);
    }

    // 전체 문의글 수 조회
    public int getInquiryCount(String type, String keyword) {
        Map<String, Object> params = new HashMap<>();

        if (type != null && keyword != null && !type.isEmpty() && !keyword.isEmpty()) {
            params.put("type", type);
            params.put("keyword", keyword);
        }
        return inquiryMapper.getInquiryCount(params);
    }


    // 문의글 작성
    public void addInquiry(Inquiry inquiry, MultipartFile file) throws IOException {

        // 파일 처리
        if (file != null && !file.isEmpty()) {
            // 파일 저장 디렉토리 확인 및 생성
            File directory = new File(FILE_STORAGE_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 고유한 파일명 생성
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String savedFileName = UUID.randomUUID().toString() + extension;

            // 파일 저장
            File savedFile = new File(directory, savedFileName);
            file.transferTo(savedFile);

            // 파일명 설정
            inquiry.setInquiryFile(savedFileName);
        }

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
        return inquiryMapper.getInquiryAnswer(inquiryNo);
    }

}