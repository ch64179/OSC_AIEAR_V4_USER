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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aiear.config.session.JwtUtil;
import com.aiear.dao.CommonDAO;
import com.aiear.dao.LoginDAO;
import com.aiear.dao.MenuDAO;
import com.aiear.dao.UserMngDAO;
import com.aiear.util.LoginUtil;
import com.aiear.util.SHA512;
import com.aiear.vo.LoginVO;
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
	private JwtUtil jwtUtil;
	
	@Autowired
	private CommonDAO commonDAO;
	
	
	@ApiOperation(value = "사용자 프로필 조회"
				, notes = "사용자 프로필 조회")
	@GetMapping(value = "getUserProfile/{user_code}.do")
	public @ResponseBody Map<String, Object> getUserProfile(
			HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable String user_code,
			MenuVO menuVO) {
	
		logger.info("■■■■■■ getUserProfile / MenuVO : {}", menuVO.beanToHmap(menuVO).toString());
		Map<String, Object> userInfo = new HashMap<String, Object>();
		
		try {
			menuVO.setUser_code(user_code);
			
			if(menuVO.getUser_code() == null || "".equals(menuVO.getUser_code())) {
				userInfo.put("msg", "유저 코드값이 없습니다.");
				res.setStatus(400);
				return userInfo;
			}
			
			userInfo = menuDAO.getUserProfileInfo(menuVO);
			
			if(userInfo.get("user_img") != null) {
				byte[] bArr = (byte[]) userInfo.get("user_img");
				byte[] base64 = Base64.encodeBase64(bArr);
				
				if(base64 != null){
					userInfo.put("hospital_img_base64", base64);
					userInfo.put("user_img_str", ("data:image/jpeg;base64," + new String(base64, "UTF-8")));
				} 
			} else {
				userInfo.put("user_img_str", null);
				userInfo.put("user_img", null);
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			menuVO.setMessage("사용자 프로필 조회 실패");
			menuVO.setResult(false);
			menuVO.setStatus(400);
			res.setStatus(400);
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
			@PathVariable String user_code,
			MenuVO menuVO) {
	
		logger.info("■■■■■■ getUserDetail / MenuVO : {}", menuVO.beanToHmap(menuVO).toString());
		Map<String, Object> userInfo = new HashMap<String, Object>();
		
		try {
			menuVO.setUser_code(user_code);
			
			if(menuVO.getUser_code() == null || "".equals(menuVO.getUser_code())) {
				userInfo.put("msg", "유저 코드값이 없습니다.");
				res.setStatus(400);
				return userInfo;
			}
			
			userInfo = menuDAO.getUserDetailInfo(menuVO);
			
			if(userInfo.get("user_img") != null){
				byte[] bArr = (byte[]) userInfo.get("user_img");
				byte[] base64 = Base64.encodeBase64(bArr);
				
				if(base64 != null){
					userInfo.put("user_img_str", ("data:image/jpeg;base64," + new String(base64, "UTF-8")));
				} 
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			menuVO.setStatus(400);
			menuVO.setResult(false);
			menuVO.setMessage("유저 상세 정보 조회 실패");
			res.setStatus(400);
		}
		
		return userInfo;
	}
	
	
	@ApiOperation(value = "사용자 닉네임 업데이트"
			, notes = "사용자 닉네임 업데이트"
					+ "\n 1. user_nm"
					+ "<br>		- (필수)")
	@PostMapping(value = "updateUserNm/{user_code}.do")
	public @ResponseBody ResponseVO updateUserNm(
			HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable String user_code,
			@RequestBody MenuVO menuVO) {
		
		menuVO.setUser_code(user_code);
		
		logger.info("■■■■■■ updateUserNm / menuVO : {}", menuVO.beanToHmap(menuVO).toString());
	
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> rslt = new HashMap<String, Object>();
		int cnt = -1;
		
		if(menuVO.getUser_code() == null || "".equals(menuVO.getUser_code())) {
			rslt.put("msg", "유저 코드 정보가 없습니다.");
			rslt.put("val", cnt);
			rsltVO.setResult(false);
			rsltVO.setData(rslt);
			res.setStatus(400);
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
			rsltVO.setResult(false);
			rsltVO.setStatus(400);
			res.setStatus(400);
		}
		
		rsltVO.setData(rslt);
		
		return rsltVO;
	}
	
	
	@ApiOperation(value = "사용자 정보 업데이트"
			, notes = "사용자 정보 업데이트"
					+ "\n 1. user_gender"
					+ "<br>		- M,F(필수)"
					+ "\n 2. user_birth"
					+ "<br>		- yyyyMmdd(필수)")
	@PostMapping(value = "updateUserInfo/{user_code}.do")
	public @ResponseBody ResponseVO updateUserInfo(
			HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable String user_code,
			@RequestBody MenuVO menuVO) {
		
		menuVO.setUser_code(user_code);
		
		logger.info("■■■■■■ updateUserInfo / menuVO : {}", menuVO.beanToHmap(menuVO).toString());
	
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> rslt = new HashMap<String, Object>();
		int cnt = -1;
		
		if(menuVO.getUser_code() == null || "".equals(menuVO.getUser_code())) {
			rslt.put("msg", "사용자 코드 정보가 없습니다.");
			rslt.put("val", cnt);
			rsltVO.setResult(false);
			rsltVO.setData(rslt);
			res.setStatus(400);
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
			rsltVO.setResult(false);
			rsltVO.setStatus(400);
			res.setStatus(400);
		}
		
		rsltVO.setData(rslt);
		
		return rsltVO;
	}
	
	
	@ApiOperation(value = "사용자 아이콘 업데이트"
			, notes = "사용자 아이콘 업데이트"
					+ "\n 1. icon_type"
					+ "<br>		- ICON01 ~ ICON08(필수)")
	@PostMapping(value = "updateUserIcon/{user_code}.do")
	public @ResponseBody ResponseVO updateUserIcon(
			HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable String user_code,
			@RequestBody MenuVO menuVO) {
		
		menuVO.setUser_code(user_code);
		
		logger.info("■■■■■■ updateUserInfo / menuVO : {}", menuVO.beanToHmap(menuVO).toString());
	
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> rslt = new HashMap<String, Object>();
		int cnt = -1;
		
		if(menuVO.getUser_code() == null || "".equals(menuVO.getUser_code())) {
			rslt.put("msg", "사용자 코드 정보가 없습니다.");
			rslt.put("val", cnt);
			rsltVO.setResult(false);
			rsltVO.setData(rslt);
			res.setStatus(400);
			return rsltVO;
		}
		
		try {
			cnt = menuDAO.updateUserIcon(menuVO);
			
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
			rsltVO.setResult(false);
			rsltVO.setStatus(400);
			res.setStatus(400);
		}
		
		rsltVO.setData(rslt);
		
		return rsltVO;
	}
	
	
	@ApiOperation(value = "비밀번호 변경"
			, notes = "비밀번호 변경"
					+ "\n 1. user_code"
					+ "<br>		- 필수값"
					+ "\n 1. user_pwd"
					+ "<br>		- 필수값"
			)
	@PostMapping(value = "updatePwdInfo.do")
	public @ResponseBody ResponseVO updatePwdInfo(
			HttpServletRequest req,
			HttpServletResponse res,
			@RequestBody MenuVO menuVO) {
		
		logger.info("■■■■■■ updatePwdInfo / loginVO : {}", menuVO.beanToHmap(menuVO).toString());
		
		ResponseVO resVO = new ResponseVO();
		
		try {
			
			if(menuVO.getUser_code() == null || "".equals(menuVO.getUser_code())){
				resVO.setMessage("유저 코드를 입력해 주세요.");
				resVO.setResult(false);
				resVO.setStatus(400);
				res.setStatus(400);
				return resVO;
			}
			
			if(menuVO.getUser_pwd() == null || "".equals(menuVO.getUser_pwd())){
				resVO.setMessage("비밀번호를 입력해 주세요.");
				resVO.setResult(false);
				resVO.setStatus(400);
				res.setStatus(400);
				return resVO;
			}
			
			Map<String, Object> srchIdInfo = menuDAO.getUserDetailInfo(menuVO);
			
			if(srchIdInfo == null){
				resVO.setMessage(menuVO.getUser_code() + " / 일치하는 계정이 없습니다.");
				resVO.setResult(false);
				resVO.setStatus(400);
			} else {
			
				// 3. 임시 비밀번호로 업데이트
				//		+ 비밀번호 암호화 처리
				String userSalt = SHA512.getSalt();
				String encPwd = SHA512.sha256(menuVO.getUser_pwd(), userSalt);
				menuVO.setTemp_pwd(encPwd);
				menuVO.setUser_salt(userSalt);
				
				
				Integer rslt = menuDAO.updateUserChngPwd(menuVO);
				
				if(rslt > 0){
					UserInfoVO userVO = new UserInfoVO();
					userVO.setUser_id(menuVO.getUser_id());
					userVO.setUser_code(srchIdInfo.get("user_code").toString());
					
					userDAO.insertUserHst(userVO);
					
					resVO.setData(srchIdInfo);
					resVO.setMessage("비밀번호 변경 성공");
					resVO.setResult(true);
				} else {
					resVO.setMessage("비밀번호 변경 실패");
					resVO.setResult(false);
					resVO.setStatus(400);
					res.setStatus(400);
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			resVO.setStatus(400);
			resVO.setMessage("비밀번호 변경 오류");
			res.setStatus(400);
		}
		
		return resVO;
	}
	
	
	@ApiOperation(value = "사용자 탈퇴 처리"
			, notes = "사용자 탈퇴 처리")
	@PostMapping(value = "deleteUserAction/{user_code}.do")
	public @ResponseBody ResponseVO deleteUserAction(
			HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable String user_code,
			@RequestBody MenuVO menuVO) {
		
		menuVO.setUser_code(user_code);
		
		logger.info("■■■■■■ deleteUserAction / menuVO : {}", menuVO.beanToHmap(menuVO).toString());
		
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> rsltMap = new HashMap<String, Object>();
		Integer cnt = -1;
		
		if(menuVO.getUser_code() == null || "".equals(menuVO.getUser_code())) {
			rsltMap.put("msg", "사용자 코드 정보가 없습니다.");
			rsltMap.put("val", cnt);
			rsltVO.setData(rsltMap);
			rsltVO.setResult(false);
			res.setStatus(400);
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
			rsltVO.setResult(false);
			res.setStatus(400);
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
					map.put("user_img_str", ("data:image/jpeg;base64," + new String(base64, "UTF-8")));
				}
				
				rsltList.add(map);
			}
			
			list.put("data", rsltList);
			list.put("size", rsltList.size());
			
		} catch(Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			res.setStatus(400);
		}
		
		return list;
	}
	
	
	@ApiOperation(value = "사용자 가족관계 신규등록"
			, notes = "사용자 가족관계 신규등록"
					+ "\n 1. family_user_code"
					+ "<br>		- (필수)"
					+ "\n 2. family_relation"
					+ "<br>		- String 30자 제한 (선택)")
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
			rsltVO.setResult(false);
			rsltVO.setStatus(400);
			res.setStatus(400);
		}
		
		rsltVO.setData(rslt);
		
		return rsltVO;
	}
	
	
	@ApiOperation(value = "회원가입(직접 등록하기)"
			, notes = "회원가입(직접 등록하기)"
					+ "\n 1. user_nm"
					+ "<br> 	- 필수값"
					+ "\n 2. user_gender"
					+ "<br> 	- 필수값(F, M)"
					+ "\n 3. user_birth"
					+ "<br> 	- 필수값(yyyyMMdd)"
//					+ "\n 4. img_file"
//					+ "<br> 	- 필수값"
					+ "\n 4. icon_type"
					+ "<br> 	- 필수값"
			)
	@PostMapping(value = "insertDirectUserInfo.do")
	public @ResponseBody ResponseVO insertDirectUserInfo(	
			HttpServletRequest req,
			HttpServletResponse res,
			@RequestParam(value = "img_file", required = false) MultipartFile img_file,
			UserInfoVO userVO) {
		
		logger.info("■■■■■■ insertDirectUserInfo / userVO : {}", userVO.beanToHmap(userVO).toString());
		
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> rslt = new HashMap<String, Object>();
		int cnt = -1;
		
		try {
			String gen_user_id = jwtUtil.getLoginIdbyToken(req);
			
			LoginVO loginVO = new LoginVO();
			loginVO.setUser_id(userVO.getUser_id());
			
			userVO.setUser_pwd("0000");
			
			String userSalt = SHA512.getSalt();
			String encPwd = SHA512.sha256(userVO.getUser_pwd(), userSalt);
			userVO.setUser_pwd(encPwd);
			userVO.setUser_salt(userSalt);
			
			byte[] b_img_file;
			
			if(img_file != null || "".equals(img_file)) {
				b_img_file = img_file.getBytes();
				userVO.setImg_file_byte(b_img_file);
			}
			
			String user_id = userDAO.getGenUserCode();
			
			userVO.setUser_code(user_id);
			userVO.setUser_id(user_id);
			userVO.setGen_by(gen_user_id);
			
			cnt = userDAO.insertDirectUserInfo(userVO);
			cnt = cnt > 0 ? userDAO.insertUserHst(userVO) : cnt; 
			
			if(cnt > 0){
				Map<String, Object> userInfo = userDAO.getUserInfo(userVO);
				
				MenuVO menuVO = new MenuVO();
				
				menuVO.setUser_code(userInfo.get("user_code").toString());
				menuVO.setFamily_user_code(user_id);
				menuVO.setFamily_relation("직접 등록");
				
				menuDAO.insertUserFamilyMapp(menuVO);
			}
			
			rslt.put("cnt", cnt);
			rslt.put("msg", "SUCCESS");
				
		} catch (Exception e) {
			// TODO: handle exception
			rslt.put("msg", e.getMessage());
			rslt.put("cnt", cnt);
			rsltVO.setResult(false);
			rsltVO.setStatus(400);
			res.setStatus(400);
		}
		
		return rsltVO;
	}
	
	
	@ApiOperation(value = "사용자 가족관계 삭제"
			, notes = "사용자 가족관계 삭제"
					+ "\n 1. family_seq"
					+ "<br> 	- 사용자 가족관계 조회(getIndUserFamilyList)에서 받는 값(필수)")
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
			rsltVO.setResult(false);
			rsltVO.setStatus(400);
			res.setStatus(400);
		}
	
		rsltVO.setData(rslt);
		
		return rsltVO;
	}
	
}
