package com.onestack.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.onestack.project.domain.MemProPortCate;
import com.onestack.project.domain.Member;

@Mapper
public interface ManagerMapper {
	
	public List<Member> getAllMember();
	
	public List<MemProPortCate> getMemProPortCate();
	
	void updateProStatus(@Param("proNo") int proNo, 
            @Param("professorStatus") Integer professorStatus, 
            @Param("screeningMsg") String screeningMsg);

	void updateMember(@Param("memberNo") int memberNo,
					  @Param("memberType") Integer memberType,
					  @Param("memberStatus") Integer memberStatus);
}
