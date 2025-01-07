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
public class Community {
	private Integer communityBoardNo;
    private Integer memberNo;
    private String communityBoardTitle;
    private String communityBoardContent;
    private Timestamp communityBoardRegDate;
    private Integer communityView;
    private String communityFile;
    private Integer communityBoardLike;
    private Integer communityBoardDislike;
    private Integer communityBoardActivation;
}
