package com.onestack.project.mapper;

import java.util.List;

import com.onestack.project.domain.Reports;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.onestack.project.domain.MemProPortPaiCate;
import com.onestack.project.domain.Member;

@Mapper
public interface ManagerMapper {
	
	public List<Member> getAllMember();

	List<MemProPortPaiCate> getMemProPortPaiCate();

	void updateProStatus(@Param("proNo") int proNo, 
            @Param("professorStatus") Integer professorStatus, 
            @Param("screeningMsg") String screeningMsg);

	void updateMember(@Param("memberNo") int memberNo,
					  @Param("memberType") Integer memberType,
					  @Param("memberStatus") Integer memberStatus);

	public Member getWithdrawalMember();

	public Member getMember();

	void addReports(Reports reports);
}
