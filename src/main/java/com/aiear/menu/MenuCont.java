package com.aiear.menu;

import io.swagger.annotations.ApiOperation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aiear.dao.CommonDAO;
import com.aiear.dao.MenuDAO;
import com.aiear.dao.UserMngDAO;
import com.aiear.vo.MenuVO;
import com.aiear.vo.ResponseVO;
import com.aiear.vo.UserInfoVO;


@RestController
@RequestMapping("/menu/*")
@RequiredArgsConstructor
public class MenuCont {
	
	/** 로그 처리용 개체 선언 */
	protected Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private MenuDAO menuDAO;

	@Autowired
	private UserMngDAO userDAO;
	
	@Autowired
	private CommonDAO commonDAO;
	
	
	@ApiOperation(value = "사용자 프로필 조회"
				, notes = "사용자 프로필 조회")
	@GetMapping(value = "getUserProfile/{user_code}.do")
	public @ResponseBody Map<String, Object> getUserProfile(
			HttpServletRequest req,
			HttpServletResponse res,
			MenuVO menuVO) {
	
		logger.info("■■■■■■ getUserProfile / MenuVO : {}", menuVO.beanToHmap(menuVO).toString());
		Map<String, Object> userInfo = new HashMap<String, Object>();
		
		try {
			userInfo = menuDAO.getUserProfileInfo(menuVO);
			
			byte[] bArr = (byte[]) userInfo.get("user_img");
			byte[] base64 = Base64.encodeBase64(bArr);
			
			if(base64 != null){
				userInfo.put("user_img_str", (new String(base64, "UTF-8")));
			} 
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userInfo;
	}
	
	
	@ApiOperation(value = "사용자 가족관계 정보 조회"
				, notes = "사용자 가족관계 정보 조회")
	@GetMapping(value = "getUserFamilyList/{user_code}.do")
	public @ResponseBody List<Map<String, Object>> getUserFamilyList(
			HttpServletRequest req,
			HttpServletResponse res,
			MenuVO menuVO) {
		
		logger.info("■■■■■■ getUserFamilyList / menuVO : {}", menuVO.beanToHmap(menuVO).toString());
		
		List<Map<String, Object>> userList = menuDAO.getUserFamilyList(menuVO);
		
		Map<String, Object> list = new HashMap<String, Object>();
		
		list.put("data", userList);
		list.put("size", userList.size());
		
		return userList;
	}
	
	
	@ApiOperation(value = "사용자 상세정보 조회"
			, notes = "사용자 상세정보 조회")
	@GetMapping(value = "getUserDetail/{user_code}.do")
	public @ResponseBody Map<String, Object> getUserDetail(
			HttpServletRequest req,
			HttpServletResponse res,
			MenuVO menuVO) {
	
		logger.info("■■■■■■ getUserDetail / MenuVO : {}", menuVO.beanToHmap(menuVO).toString());
		Map<String, Object> userInfo = new HashMap<String, Object>();
		
		try {
			userInfo = menuDAO.getUserDetailInfo(menuVO);
			
			byte[] bArr = (byte[]) userInfo.get("user_img");
			byte[] base64 = Base64.encodeBase64(bArr);
			
			if(base64 != null){
				userInfo.put("user_img_str", (new String(base64, "UTF-8")));
			} 
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userInfo;
	}
	
	
	@ApiOperation(value = "사용자 닉네임 업데이트"
			, notes = "사용자 닉네임 업데이트"
					+ "\n 1. user_code"
					+ "<br>		- (필수)"
					+ "\n 2. user_nm"
					+ "<br>		- (필수)")
	@PostMapping(value = "updateUserNm.do")
	public @ResponseBody ResponseVO updateUserNm(
			HttpServletRequest req,
			HttpServletResponse res,
			@RequestBody MenuVO menuVO) {
		
		logger.info("■■■■■■ updateUserNm / menuVO : {}", menuVO.beanToHmap(menuVO).toString());
	
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> rslt = new HashMap<String, Object>();
		int cnt = -1;
		
		if(menuVO.getUser_code() == null || "".equals(menuVO.getUser_code())) {
			rslt.put("msg", "사용자 코드 정보가 없습니다.");
			rslt.put("val", cnt);
			rsltVO.setResult(false);
			rsltVO.setData(rslt);
			return rsltVO;
		}
		
		try {
			cnt = menuDAO.updateUserNm(menuVO);
			
			UserInfoVO userVO = new UserInfoVO();
			userVO.setUser_code(menuVO.getUser_code());
			cnt = cnt > 0 ? userDAO.insertUserHst(userVO) : cnt; 
			
			rslt.put("cnt", cnt);
			rslt.put("msg", cnt > 0 ? "SUCCESS" : "FAIL");
			rsltVO.setResult(cnt > 0 ? true : false);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			rslt.put("msg", e.getMessage());
			rslt.put("cnt", cnt);
			rsltVO.setStatus(400);
		}
		
		rsltVO.setData(rslt);
		
		return rsltVO;
	}
	
	
	@ApiOperation(value = "사용자 정보 업데이트"
			, notes = "사용자 정보 업데이트"
					+ "\n 1. user_code"
					+ "<br>		- (필수)"
					+ "\n 2. user_gender"
					+ "<br>		- (필수)"
					+ "\n 3. user_birth"
					+ "<br>		- (필수)")
	@PostMapping(value = "updateUserInfo.do")
	public @ResponseBody ResponseVO updateUserInfo(
			HttpServletRequest req,
			HttpServletResponse res,
			@RequestBody MenuVO menuVO) {
		
		logger.info("■■■■■■ updateUserInfo / menuVO : {}", menuVO.beanToHmap(menuVO).toString());
	
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> rslt = new HashMap<String, Object>();
		int cnt = -1;
		
		if(menuVO.getUser_code() == null || "".equals(menuVO.getUser_code())) {
			rslt.put("msg", "사용자 코드 정보가 없습니다.");
			rslt.put("val", cnt);
			rsltVO.setResult(false);
			rsltVO.setData(rslt);
			return rsltVO;
		}
		
		try {
			cnt = menuDAO.updateUserDetail(menuVO);
			
			UserInfoVO userVO = new UserInfoVO();
			userVO.setUser_code(menuVO.getUser_code());
			cnt = cnt > 0 ? userDAO.insertUserHst(userVO) : cnt; 
			
			rslt.put("cnt", cnt);
			rslt.put("msg", cnt > 0 ? "SUCCESS" : "FAIL");
			rsltVO.setResult(cnt > 0 ? true : false);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			rslt.put("msg", e.getMessage());
			rslt.put("cnt", cnt);
			rsltVO.setStatus(400);
		}
		
		rsltVO.setData(rslt);
		
		return rsltVO;
	}
	
	
	@ApiOperation(value = "사용자 탈퇴 처리"
			, notes = "사용자 탈퇴 처리")
	@PostMapping(value = "deleteUserAction.do")
	public @ResponseBody ResponseVO deleteUserAction(
			HttpServletRequest req,
			HttpServletResponse res,
			@RequestBody MenuVO menuVO) {
		
		logger.info("■■■■■■ deleteUserAction / menuVO : {}", menuVO.beanToHmap(menuVO).toString());
		
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> rsltMap = new HashMap<String, Object>();
		Integer cnt = -1;
		
		if(menuVO.getUser_code() == null || "".equals(menuVO.getUser_code())) {
			rsltMap.put("msg", "사용자 코드 정보가 없습니다.");
			rsltMap.put("val", cnt);
			rsltVO.setResult(false);
			rsltVO.setData(rsltMap);
			return rsltVO;
		}
		
		try {
			cnt = menuDAO.deleteUserAction(menuVO);
			
			UserInfoVO userVO = new UserInfoVO();
			userVO.setUser_code(menuVO.getUser_code());
			cnt = cnt > 0 ? userDAO.insertUserHst(userVO) : cnt; 
			
			rsltMap.put("cnt", cnt);
			rsltMap.put("msg", cnt > 0 ? "SUCCESS" : "FAIL");
			rsltVO.setResult(cnt > 0 ? true : false);
			
		} catch (Exception e) {
			// TODO: handle exception
			rsltMap.put("msg", e.getMessage());
			rsltMap.put("val", cnt);
			rsltVO.setStatus(400);
		}
		
		rsltVO.setData(rsltMap);
		
		return rsltVO;
	}
	
	
	@ApiOperation(value = "가족관리 가족관계 정보 조회"
				, notes = "가족관리 가족관계 정보 조회")
	@GetMapping(value = "getIndUserFamilyList/{user_code}.do")
	public @ResponseBody Map<String, Object> getIndUserFamilyList(
			HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable String user_code,
			MenuVO menuVO) {
		
		logger.info("■■■■■■ getIndUserFamilyList / menuVO : {}", menuVO.beanToHmap(menuVO).toString());
		List<Map<String, Object>> rsltList = new ArrayList<Map<String,Object>>();
		Map<String, Object> list = new HashMap<String, Object>();
		
		try {
			List<Map<String, Object>> userList = menuDAO.getIndUserFamilyList(menuVO);
			
			for(Map<String, Object> map : userList){
				byte[] bArr = (byte[]) map.get("user_img");
				byte[] base64 = Base64.encodeBase64(bArr);
				
				if(base64 != null){
					map.put("user_img_str", (new String(base64, "UTF-8")));
				}
				
				rsltList.add(map);
			}
			
			list.put("data", rsltList);
			list.put("size", rsltList.size());
			
		} catch(Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	@ApiOperation(value = "사용자 가족관계 신규등록"
			, notes = "사용자 가족관계 신규등록"
					+ "\n 1. family_user_code"
					+ "<br>		- (필수)"
					+ "\n 2. family_relation"
					+ "<br>		- (선택)")
	@PostMapping(value = "insertUserFamilyMapp/{user_code}.do")
	public @ResponseBody ResponseVO insertUserFamilyMapp(
			HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable String user_code,
			@RequestBody MenuVO menuVO) {
		
		menuVO.setUser_code(user_code);
		
		logger.info("■■■■■■ insertUserFamilyMapp / menuVO : {}", menuVO.beanToHmap(menuVO).toString());
		
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> rslt = new HashMap<String, Object>();
		int cnt = -1;
		try {
			cnt = menuDAO.insertUserFamilyMapp(menuVO);
			
			rslt.put("cnt", cnt);
			rslt.put("msg", "SUCCESS");
			rsltVO.setResult(true);
			
		} catch (Exception e) {
			// TODO: handle exception
			rslt.put("msg", e.getMessage());
			rslt.put("cnt", cnt);
			rsltVO.setStatus(400);
		}
		
		rsltVO.setData(rslt);
		
		return rsltVO;
	}
	
	
	@ApiOperation(value = "사용자 가족관계 삭제"
			, notes = "사용자 가족관계 삭제"
					+ "\n 1. family_seq"
					+ "<br> 	- (필수)")
	@PostMapping(value = "deleteUserFamilyMapp/{user_code}.do")
	public @ResponseBody ResponseVO updateUserFamilyMapp(
			HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable String user_code,
			@RequestBody MenuVO menuVO) {
	
		menuVO.setUser_code(user_code);
		logger.info("■■■■■■ deleteUserFamilyMapp / menuVO : {}", menuVO.beanToHmap(menuVO).toString());
		
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> rslt = new HashMap<String, Object>();
		int cnt = -1;
		
		try {
			cnt = menuDAO.deleteUserFamilyMapp(menuVO);
			
			rslt.put("cnt", cnt);
			rslt.put("msg", "SUCCESS");
			rsltVO.setResult(true);
		} catch (Exception e) {
			// TODO: handle exception
			rslt.put("msg", e.getMessage());
			rslt.put("cnt", cnt);
			rsltVO.setStatus(400);
		}
	
		rsltVO.setData(rslt);
		
		return rsltVO;
	}
	
}
