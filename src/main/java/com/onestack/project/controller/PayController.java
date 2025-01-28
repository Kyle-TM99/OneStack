package com.onestack.project.controller;

import com.onestack.project.domain.Pay;
import com.onestack.project.domain.PaymentVerificationRequest;
import com.onestack.project.service.PayService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Slf4j
@Controller
public class PayController {

    @Autowired
    PayService payService;


    /* 결제 요청 폼 */
    @GetMapping("/payForm")
    public String getPayForm(Model model, int quotationNo) {

        Map<String, Object> quotationModelMap = payService.getPayForm(quotationNo);
        model.addAllAttributes(quotationModelMap);

        return "views/payForm";
    }

    /* 결제 완료 폼 */
    @GetMapping("/payDoneForm")
    public String getPayDoneForm(Model model, @RequestParam int quotationNo) {
        Map<String, Object> payModelMap = payService.getPayDoneForm(quotationNo);
        model.addAllAttributes(payModelMap);

        return "views/payDoneForm";
    }

    /* 결제 검증 */
    @PostMapping("/api/payment/verify")
    public ResponseEntity<String> paymentVerify(@RequestBody PaymentVerificationRequest request) throws Exception {

        log.info("/verify : " + request.getImpUid());
        boolean isVerified = payService.verifyPayment(request.getImpUid(), request.getQuotationNo(), request.getPaidAmount());

        if (isVerified) {
            // 최종 결제 처리 - DB(결제 내역) 작업

            Pay pay = new Pay();
            pay.setQuotationNo(request.getQuotationNo()); // 주문 정보의 quotationNo
            pay.setMemberNo(request.getMemberNo()); // 주문 정보의 memberNo
            pay.setPayType("카카오페이"); // 결제 타입 예시
            pay.setPayContent(request.getPayContent()); // 결제 타입 예시
            pay.setPayPrice(request.getPaidAmount()); // 결제 금액
            pay.setPayStatus(true); // 결제 완료 상태 (1)

            // 결제 정보 DB에 저장
            payService.insertPay(pay);


            return ResponseEntity.ok(String.valueOf(request.getQuotationNo()));
        }

        return ResponseEntity.status(400).body("결제 검증 실패");
    }

}
