package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemProAdInfoCate {
    Member member;
    Professional professional;
    ProfessionalAdvancedInformation proAdInfo;
    Category category;
}
