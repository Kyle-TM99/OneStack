package com.onestack.project.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reports {
	private int reportsNo;
    private int memberNo;
    private int reportedMemberNo;
    private String reportsType;
    private String reportsTarget;
    private String reportsReason;
    private boolean reportsStatus;
    private Timestamp reportsDate;
}
