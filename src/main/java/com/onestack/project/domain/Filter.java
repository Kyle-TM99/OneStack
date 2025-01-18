package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Filter {
    private int filterNo;
    private int itemNo;
    private int filterQuestionNo;
    private String filterQuestion;
    private String filterOption;
}
