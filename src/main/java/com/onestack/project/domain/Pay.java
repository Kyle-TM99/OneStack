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
public class Pay {
    private int payNo;
    private int estimationNo;
    private int memberNo;
    private String payType;
    private String payContent;
    private int payPrice;
    private Boolean payStatus;
    private Timestamp payDate;

}
