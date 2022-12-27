package com.aiear.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.aiear.vo.SMTPVO;

@Mapper
@Repository
public interface SMTPDAO {

	/**
	 * Description	: 
	 * @method		: insertSMTPSendHst
	 * @author		: pcw
	 * @date		: 2022. 12. 20.
	 * @param vo
	 * @return
	 */
	public int insertSMTPSendHst(SMTPVO vo);
	
}
