package com.onestack.project.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quotation {
	private int quotationNo;
	private int matchingNo;
	private int memberNo;
	private int proNo;
	private String quotationContent;
	private BigDecimal quotationPrice;
	private Timestamp quotationRegDate;
}
