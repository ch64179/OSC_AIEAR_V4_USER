package com.aiear.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.aiear.vo.MenuVO;

@Mapper
@Repository
public interface MenuDAO {

	
	/**
	 * Description	: 
	 * @method		: getUserProfileInfo
	 * @author		: pcw
	 * @date		: 2022. 12. 24.
	 * @param vo
	 * @return
	 */
	public Map<String, Object> getUserProfileInfo(MenuVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: getUserFamilyList
	 * @author		: pcw
	 * @date		: 2022. 12. 23.
	 * @param vo
	 * @return
	 */
	public List<Map<String, Object>> getUserFamilyList(MenuVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: getUserDetailInfo
	 * @author		: pcw
	 * @date		: 2022. 12. 24.
	 * @param vo
	 * @return
	 */
	public Map<String, Object> getUserDetailInfo(MenuVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: updateUserNm
	 * @author		: pcw
	 * @date		: 2022. 12. 24.
	 * @param vo
	 * @return
	 */
	public int updateUserNm(MenuVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: updateUserDetail
	 * @author		: pcw
	 * @date		: 2022. 12. 24.
	 * @param vo
	 * @return
	 */
	public int updateUserDetail(MenuVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: deleteUserAction
	 * @author		: pcw
	 * @date		: 2022. 12. 24.
	 * @param vo
	 * @return
	 */
	public int deleteUserAction(MenuVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: getIndUserFamilyList
	 * @author		: pcw
	 * @date		: 2022. 12. 23.
	 * @param vo
	 * @return
	 */
	public List<Map<String, Object>> getIndUserFamilyList(MenuVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: insertUserFamilyMapp
	 * @author		: pcw
	 * @date		: 2022. 12. 25.
	 * @param vo
	 * @return
	 */
	public int insertUserFamilyMapp(MenuVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: deleteUserFamilyMapp
	 * @author		: pcw
	 * @date		: 2022. 12. 25.
	 * @param vo
	 * @return
	 */
	public int deleteUserFamilyMapp(MenuVO vo);
	
}
