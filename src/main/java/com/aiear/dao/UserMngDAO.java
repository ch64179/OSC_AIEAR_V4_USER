package com.aiear.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.aiear.vo.MenuVO;
import com.aiear.vo.UserInfoVO;

@Mapper
@Repository
public interface UserMngDAO {

	/**
	* @methodName    : getUserInfo
	* @author        : pcw
	* @date        : 2022.11.18
	* @return
	*/
	public List<Map<String, Object>> getUserListInfo(UserInfoVO vo);
	
	
	/**
	* @methodName    : getUserDetailInfo
	* @author        : pcw
	* @date        : 2022.11.19
	* @param vo
	* @return
	*/
	public Map<String, Object> getUserDetailInfo(UserInfoVO vo);
	
	
	/**
	* @methodName    : getUserRelationList
	* @author        : pcw
	* @date        : 2022.11.19
	* @param vo
	* @return
	*/
	public List<Map<String, Object>> getUserRelationList(UserInfoVO vo);
	
	
	/**
	* @methodName    : deleteUserAction
	* @author        : pcw
	* @date        : 2022.11.19
	* @return
	*/
	public int deleteUserAction(UserInfoVO vo);
	
	
	/**
	* @methodName    : insertUserHst
	* @author        : pcw
	* @date        : 2022.11.19
	* @param vo
	* @return
	*/
	public int insertUserHst(UserInfoVO vo);
	
	
	/**
	* @methodName    : getUserCodeValidChk
	* @author        : pcw
	* @date        : 2022.11.19
	* @param vo
	* @return
	*/
	public int getUserCodeDupChk(UserInfoVO vo);
	
	
	/**
	* @methodName    : updateUserDetail
	* @author        : pcw
	* @date        : 2022.11.19
	* @param vo
	* @return
	*/
	public int updateUserDetail(UserInfoVO vo);

	
	/**
	* @methodName    : insertUserFamilyMapp
	* @author        : pcw
	* @date        : 2022.11.19
	* @param vo
	* @return
	*/
	public int insertUserFamilyMapp(UserInfoVO vo);
	
	
	/**
	* @methodName    : updateUserFamilyMapp
	* @author        : pcw
	* @date        : 2022.11.19
	* @param vo
	* @return
	*/
	public int updateUserFamilyMapp(UserInfoVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: insertUserInfo
	 * @author		: pcw
	 * @date		: 2022. 12. 20.
	 * @param vo
	 * @return
	 */
	public int insertUserInfo(UserInfoVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: getGenUserCode
	 * @author		: pcw
	 * @date		: 2022. 12. 20.
	 * @return
	 */
	public String getGenUserCode();
	
	
	/**
	 * Description	: 
	 * @method		: getTokenByUserInfo
	 * @author		: pcw
	 * @date		: 2023. 3. 19.
	 * @param vo
	 * @return
	 */
	public Map<String, Object> getTokenByUserInfo(UserInfoVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: insertDirectUserInfo
	 * @author		: pcw
	 * @date		: 2023. 3. 21.
	 * @param vo
	 * @return
	 */
	public int insertDirectUserInfo(UserInfoVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: getUserInfo
	 * @author		: pcw
	 * @date		: 2023. 3. 21.
	 * @param vo
	 * @return
	 */
	public Map<String, Object> getUserInfo(UserInfoVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: getDupleCheck
	 * @author		: pcw
	 * @date		: 2023. 3. 24.
	 * @param vo
	 * @return
	 */
	public int getDupleCheck(UserInfoVO vo);
	
	
	/**
	 * Description	: 
	 * @method		: insertSocialUserInfo
	 * @author		: pcw
	 * @date		: 2023. 3. 31.
	 * @param vo
	 * @return
	 */
	public int insertSocialUserInfo(UserInfoVO vo);
}
