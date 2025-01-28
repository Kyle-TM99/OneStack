package com.onestack.project.mapper;

import com.onestack.project.domain.MemPay;
import com.onestack.project.domain.Pay;
import com.onestack.project.domain.Quotation;
import com.onestack.project.domain.SurveyWithCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PayMapper {

    // 결제 폼 요청
    public List<Quotation> getPayForm(@Param("quotationNo") int quotationNo);

    // 결제 검증을 위한 데이터 추출
    public int getPrice(@Param("quotationNo") int quotationNo);

    // 결제 DB 저장
    public void insertPay(Pay pay);

    // 결제 완료 폼 요청
    public List<MemPay> getPayDoneForm(@Param("quotationNo") int quotationNo);
}
