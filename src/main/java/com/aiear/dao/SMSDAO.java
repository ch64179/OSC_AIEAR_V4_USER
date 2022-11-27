package com.aiear.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.aiear.vo.SMSVO;

@Mapper
@Repository
public interface SMSDAO {

	/**
	 * Description	: 
	 * @method		: normalLoginIdProcess
	 * @author		: pcw
	 * @date		: 2022. 11. 25.
	 * @param vo
	 * @return
	 */
	public int insertSMSSendHst(SMSVO vo);
	
}
