package com.onestack.project.service;

import com.onestack.project.domain.Inquiry;
import com.onestack.project.mapper.InquiryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InquiryService {

    @Autowired
    private InquiryMapper inquiryMapper;

    public List<Inquiry> getInquiry(int startRow, int num) {
        Map<String, Object> params = new HashMap<>();
        params.put("startRow", startRow);
        params.put("num", num);
        return inquiryMapper.getInquiry(params); // XML에서 정의한 getInquiry 메서드 호출
    }
    
    public void addInquiry(Inquiry inquiry) {
        inquiryMapper.addInquiry(inquiry);
    }
    
    public Inquiry getInquiryById(int inquiryNo) {
        return inquiryMapper.getInquiryById(inquiryNo);
    }
}
