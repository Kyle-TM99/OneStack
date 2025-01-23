package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estimation {
    private int estimationNo;
    private int memberNo;
    private int proNo;
    private int itemNo;
    private String estimationContent;
    private int estimationPrice;
    private String estimationMsg;
    private Boolean estimationIsread;
}
