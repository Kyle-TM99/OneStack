package com.onestack.project.mapper;

import com.onestack.project.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PayMapper {

    // 결제 폼 요청
    public MemProEstimation getPayForm(@Param("estimationNo") int estimationNo);

    // 결제 검증을 위한 데이터 추출
    public int getPrice(@Param("estimationNo") int estimationNo);

    // 결제 DB 저장
    public void insertPay(Pay pay);

    // 결제 완료 폼 요청
    public MemPay getPayDoneForm(@Param("payNo") int payNo);

    // 결제 번호 가져오기
    public int getPayNo(@Param("estimationNo") int estimationNo);

    // payNo로 전문가 조회
    public int findByPayNo(@Param("payNo") int payNo);

    // estimationNo으로 전문가 조회
    public int findByEstimationNo(@Param("estimationNo") int estimationNo);

    // proNo로 전문가 조회
    public Professional getPro(@Param("proNo") int proNo);

    // 전문가 정보 업데이트 (평균 가격 수정)
    public void updateProPrice(@Param("averagePrice") int averagePrice, @Param("proNo") int proNo);

    // 전문가 외주 갯수 가져오기
    public int getPayCount(@Param("proNo") int proNo);

    // 결제 내역 가져오기
    public List<MemPay> getReceipt(@Param("memberNo") int memberNo);

    // 결제 횟수 가져오기
    public int getMemPayCount(@Param("memberNo") int memberNo);

    // 전문가 고용 횟수 증가
    public void updateStudentCount(@Param("proNo") int proNo);

    // 페이징된 결제 내역 가져오기
    public List<MemPay> getReceiptWithPaging(@Param("memberNo") int memberNo, 
                                           @Param("offset") int offset, 
                                           @Param("limit") int limit);

}
