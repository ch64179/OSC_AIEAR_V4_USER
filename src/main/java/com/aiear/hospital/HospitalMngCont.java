package com.aiear.hospital;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aiear.dao.CommonDAO;
import com.aiear.dao.HospitalMngDAO;
import com.aiear.vo.HospitalInfoVO;


@RestController
@RequestMapping("/hospital/*")
@RequiredArgsConstructor
public class HospitalMngCont {
	
	/** 로그 처리용 개체 선언 */
	protected Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private HospitalMngDAO hsptDAO;

	@Autowired
	private CommonDAO commonDAO;
	
	
	@ApiOperation(value = "병원 정보 리스트 조회"
				, notes = "병원 정보 리스트 조회"
						+ "<br> ** > order_type, oder_type2 2개다 있어야지 정렬**"
						+ "<br> ** > raw_cnt, page_cnt 2개다 있어야지 Paginatoin 가능**"
						+ "\n 1. hospital_addr"
						+ "<br> 	- LIKE 검색"
						+ "\n 2. order_type"
						+ "<br> 	- Default : 가입일(GEN_DT)"
						+ " - 활동 상태(USE_YN), 병원 이름(HOSPITAL_NM), 병원 ID(HOSPITAL_ID), 생성일(GEN_DT)"
						+ "\n 3. order_type2"
						+ "<br> 	- Default : 내림차순(DESC)"
						+ "<br>  - 오름차순(ASC), 내림차순(DESC)"
						+ "\n 4. raw_cnt"
						+ "<br> 	- 페이지별 로우데이터 갯수"
						+ "\n 5. page_cnt"
						+ "<br>  - 페이지 선택"
						+ "\n"
						+ "\n hospital_flag : RUN(진료 중), REST(휴계 중), IDLE(휴무일)"
				)
	@GetMapping(value = "getHospitalList.do")
	public @ResponseBody Map<String, Object> getHospitalList(
			HttpServletRequest req,
			HttpServletResponse res,
			HospitalInfoVO hsptInfoVO) {
		
		logger.info("■■■■■■ getHospitalList / hsptInfoVO : {}", hsptInfoVO.beanToHmap(hsptInfoVO).toString());
		List<Map<String, Object>> rsltList = new ArrayList<Map<String,Object>>();
		Map<String, Object> list = new HashMap<String, Object>();
		
		try {
			List<Map<String, Object>> hsptList = hsptDAO.getHospitalList(hsptInfoVO);
			
			for(Map<String, Object> hsptInfo : hsptList){
				byte[] bArr = (byte[]) hsptInfo.get("hospital_img");
				byte[] base64 = Base64.encodeBase64(bArr);
				
				if(base64 != null){
					hsptInfo.put("hospital_img_str", ("data:image/jpeg;base64," + new String(base64, "UTF-8")));
				}
				
				rsltList.add(hsptInfo);
			}
			
			list.put("data", rsltList);
			list.put("size", rsltList.size());
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	@ApiOperation(value = "병원 상세 정보 조회"
			, notes = "병원 상세 정보 조회"
					+ "\n 1. hospital_id"
					+ "<br> 	- 필수값")
	@GetMapping(value = "getHospitalDetail/{hospital_id}.do")
	public @ResponseBody Map<String, Object> getHospitalDetail(
			HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable String hospital_id,
			HospitalInfoVO hsptInfoVO) {

		logger.info("■■■■■■ getHospitalDetail / hsptInfoVO : {}", hsptInfoVO.beanToHmap(hsptInfoVO).toString());
		Map<String, Object> hsptInfo = new HashMap<String, Object>();
		
		try {
			hsptInfoVO.setHospital_id(hospital_id);
			
			if(hsptInfoVO.getHospital_id() == null || "".equals(hsptInfoVO.getHospital_id())) {
				hsptInfo.put("msg", "병원 ID값이 없습니다.");
				res.setStatus(400);
				return hsptInfo;
			}
			
			hsptInfo = hsptDAO.getHospitalDetail(hsptInfoVO);
			
			byte[] bArr = (byte[]) hsptInfo.get("hospital_img");
			byte[] base64 = Base64.encodeBase64(bArr);
			
			if(base64 != null){
				hsptInfo.put("hospital_img_base64", base64);
				hsptInfo.put("hospital_img_str", ("data:image/jpeg;base64," + new String(base64, "UTF-8")));
			} 
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hsptInfoVO.setStatus(400);
			hsptInfoVO.setMessage("병원 상세 정보 조회 실패");
			res.setStatus(400);
		}
		
		return hsptInfo;
	}
	
	
	@ApiOperation(value = "병원 진료시간 조회"
			, notes = "병원 진료시간 조회"
					+ "\n 1. hospital_id"
					+ "<br> 	- 필수값")
	@GetMapping(value = "getHospitalClinicList/{hospital_id}.do")
	public @ResponseBody Map<String, Object> getHospitalClinicList(
			HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable String hospital_id,
			HospitalInfoVO hsptInfoVO) {
		
		Map<String, Object> list = new HashMap<String, Object>();
		
		hsptInfoVO.setHospital_id(hospital_id);
		
		if(hsptInfoVO.getHospital_id() == null || "".equals(hsptInfoVO.getHospital_id())) {
			res.setStatus(400);
			return list;
		}
		
		logger.info("■■■■■■ getHospitalClinicList / hsptInfoVO : {}", hsptInfoVO.beanToHmap(hsptInfoVO).toString());
		List<Map<String, Object>> hsptList = hsptDAO.getHospitalClinicList(hsptInfoVO);
		
		list.put("data", hsptList);
		list.put("size", hsptList.size());
		
		return list;
	}
}
