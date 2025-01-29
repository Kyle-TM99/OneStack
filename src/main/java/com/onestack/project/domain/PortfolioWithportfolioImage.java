package com.onestack.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioWithportfolioImage {
    private Portfolio portfolio;
    private PortfolioImage portfolioImage;
}
