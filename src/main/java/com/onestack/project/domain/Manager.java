package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Manager {
	private int managerNo;
	private String managerName;
	private String managerPass;
	private int memberType;
	private String managerImage;
	private int managerType;
}
