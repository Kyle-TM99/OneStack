package com.onestack.project.controller;

import com.onestack.project.domain.*;
import com.onestack.project.service.MemberService;
import com.onestack.project.service.PayService;
import com.onestack.project.service.ProService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class PayController {

    @Autowired
    PayService payService;
    @Autowired
    private ProService proService;
    @Autowired
    private MemberService memberService;


    /* 결제 요청 폼 */
    @GetMapping("/payForm")
    public String getPayForm(Model model, int quotationNo) {

        MemProEstimation payList = payService.getPayForm(quotationNo);
        model.addAttribute(payList);

        return "views/payForm";
    }

    /* 결제 완료 폼 */
    @GetMapping("/payDoneForm")
    @ResponseBody
    public MemPay getPayDoneForm(@RequestParam int payNo) {
        MemPay payDone = payService.getPayDoneForm(payNo);
        return payDone;  // 이 객체는 자동으로 JSON 형식으로 변환됩니다.
    }


    /* 결제 검증 */
    @PostMapping("/api/payment/verify")
    public ResponseEntity<String> paymentVerify(@RequestBody PaymentVerificationRequest request) throws Exception {

        log.info("/verify : " + request.getImpUid());
        boolean isVerified = payService.verifyPayment(request.getImpUid(), request.getEstimationNo(), request.getPaidAmount());

        int estimationNo = request.getEstimationNo();

        if (isVerified) {
            // 최종 결제 처리 - DB(결제 테이블) 작업

            Pay pay = new Pay();
            pay.setEstimationNo(request.getEstimationNo()); // 주문 정보의 quotationNo
            pay.setMemberNo(request.getMemberNo()); // 주문 정보의 memberNo
            // 결제 타입
            if(request.getChannelNum() == 1) {
                pay.setPayType("카카오페이");
            } else if (request.getChannelNum() == 2) {
                pay.setPayType("KG이니시스");
            }
            pay.setPayContent(request.getPayContent()); // 결제 내용
            pay.setPayPrice(request.getPaidAmount()); // 결제 금액
            pay.setPayStatus(true); // 결제 완료 상태 (1)

            // 결제 정보 DB에 저장
            payService.insertPay(pay);

            // 방금 결제한 결제 내역의 payNo를 가져오기
            Pay pay1 = new Pay();
            int payNo = payService.getPayNo(estimationNo);


            return ResponseEntity.ok(String.valueOf(payNo));
        }

        return ResponseEntity.status(400).body("결제 검증 실패");
    }

    /* 전문가 평균 가격 수정 및 견적 과정 수정*/
    @PostMapping("/updateAveragePrice")
    public ResponseEntity<Map<String, String>> updateAveragePrice(@RequestBody Map<String, String> payload) {
        String payNo = payload.get("payNo");
        payService.updateAveragePrice(Integer.parseInt(payNo));

        Map<String, String> response = new HashMap<>();
        response.put("message", "전문가 평균 가격 수정 성공!");

        int estimationNo = payService.findByPayNo(Integer.parseInt(payNo));

        memberService.updateEstimationProgress(estimationNo, 3);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/payReceipt/{memberNo}")
    public String payReceipt(Model model, @PathVariable("memberNo") int memberNo) {
        List<MemPay> recipetList = payService.getReceipt(memberNo);
        int payCount = payService.getMemPayCount(memberNo);

        model.addAttribute("recipetList", recipetList);
        model.addAttribute("payCount", payCount);
        return "views/payReceiptForm";
    }






}
