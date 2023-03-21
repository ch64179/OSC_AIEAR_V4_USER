package com.aiear.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.aiear.vo.ApiMngVO;

@Mapper
@Repository
public interface ApiMngDAO {

	/**
	 * Description	: 
	 * @method		: insertApiCallLog
	 * @author		: pcw
	 * @date		: 2023. 1. 8.
	 * @param vo
	 * @return
	 */
	public int insertApiCallLog(Map<String, Object> map);
	
	
	/**
	 * Description	: 
	 * @method		: isnertInferenceInfo
	 * @author		: pcw
	 * @date		: 2023. 1. 18.
	 * @return
	 */
	public int insertInferenceInfo(Map<String, Object> map);
}
