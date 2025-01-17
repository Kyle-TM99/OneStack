package com.onestack.project.mapper;

import com.onestack.project.domain.Inquiry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InquiryMapper {
	
    List<Inquiry> getInquiry(Map<String, Object> params); // 모든 문의글 조회
    
    public void addInquiry(Inquiry inquiry);
    
    public Inquiry getInquiryById(int inquiryNo);
}