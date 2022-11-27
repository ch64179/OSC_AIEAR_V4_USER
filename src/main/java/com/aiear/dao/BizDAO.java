package com.aiear.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.aiear.vo.RawDataVO;
import com.aiear.vo.StatVO;

@Mapper
@Repository
public interface BizDAO {

	/**
	 * 현재시간을 DB에서 조회한다.
	 * @return
	 */
	public String getCurrentDateTime();
	
	
}
