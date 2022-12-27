package com.aiear.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.aiear.vo.LoginVO;
import com.aiear.vo.UserInfoVO;

@Mapper
@Repository
public interface LoginDAO {

	/**
	 * Description	: 
	 * @method		: normalLoginIdProcess
	 * @author		: pcw
	 * @date		: 2022. 11. 25.
	 * @param vo
	 * @return
	 */
	public Map<String, Object> normalLoginIdProcess(LoginVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: normalLoginPwdProcess
	 * @author		: pcw
	 * @date		: 2022. 11. 27.
	 * @param vo
	 * @return
	 */
	public Map<String, Object> normalLoginPwdProcess(LoginVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: searchIdPwdInfo
	 * @author		: pcw
	 * @date		: 2022. 11. 27.
	 * @param vo
	 * @return
	 */
	public Map<String, Object> searchIdPwdInfo(LoginVO vo);

	
	/**
	 * Description	: 
	 * @method		: updateUserTempPwd
	 * @author		: pcw
	 * @date		: 2022. 11. 27.
	 * @param vo
	 * @return
	 */
	public int updateUserTempPwd(LoginVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: updateLoginInfo
	 * @author		: pcw
	 * @date		: 2022. 11. 30.
	 * @param vo
	 * @return
	 */
	public int updateLoginInfo(LoginVO vo);

}
