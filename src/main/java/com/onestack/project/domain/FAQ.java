package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FAQ {
	private int faqNo;
    private boolean faqType;
    private String faqQuestion;
    private String faqResponse;
}
