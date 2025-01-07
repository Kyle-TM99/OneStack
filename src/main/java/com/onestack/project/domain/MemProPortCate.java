package com.onestack.project.domain;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemProPortCate {
	private Member member;
	private Professional professional;
	private Portfolio portfolio;
	private Category category;
}
