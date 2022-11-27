package com.aiear.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.aiear.vo.CommonCdVO;
import com.aiear.vo.SysPolicyVO;

@Mapper
@Repository
public interface CommonDAO {

	/**
	 * 현재시간 DB 조회
	* @methodName    : isAlive
	* @author        : pcw
	* @date        : 2022.11.18
	* @return
	*/
	public String isAlive();
	
	
	/**
	* @methodName    : getCommonCodeList
	* @author        : pcw
	* @date        : 2022.11.18
	* @param param(cat_cd : 필수, cd : 옵션)
	* @return
	*/
	public List<CommonCdVO> getCommonCodeList(CommonCdVO param);
	
	
}
