package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerificationRequest {
    String impUid;
    String merchantUid;
    String payMethod;
    int paidAmount;
    String buyerName;
    String buyerPhone;
    Timestamp payTime;
    int quotationNo;
    int memberNo;
    String payContent;
}
