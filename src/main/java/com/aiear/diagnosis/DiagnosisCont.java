package com.aiear.diagnosis;

import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aiear.config.session.JwtUtil;
import com.aiear.dao.CommonDAO;
import com.aiear.dao.DiagnosisDAO;
import com.aiear.vo.DiagnosisVO;
import com.aiear.vo.ResponseVO;


@RestController
@RequestMapping("/diagnosis/*")
@RequiredArgsConstructor
public class DiagnosisCont {
	
	/** 로그 처리용 개체 선언 */
	protected Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private DiagnosisDAO diaDAO;

	@Autowired
	private CommonDAO commonDAO;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@ApiOperation(value = "청각 검사 결과 저장"
			, notes = "청각 검사 결과 저장"
					+ "\n 1. json_data"
					+ "<br>		- (필수)")
	@PostMapping(value = "insertEarDiagnosis/{user_code}.do")
	public @ResponseBody ResponseVO insertEarDiagnosis(
			HttpServletRequest req,
			HttpServletResponse res,
			@PathVariable String user_code,
			@RequestParam(value = "json_data", required = true) String json_data) {
		
		DiagnosisVO diaVO = new DiagnosisVO();
		
		diaVO.setGen_by(user_code);
		diaVO.setJson_data(json_data);
		
		logger.info("■■■■■■ insertEarDiagnosis / DiagnosisVO : {}", diaVO.beanToHmap(diaVO).toString());
		
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> rslt = new HashMap<String, Object>();
		int cnt = -1;
		
		try {
			cnt = diaDAO.insertEarDiagnosis(diaVO);
			
			rslt.put("cnt", cnt);
			rslt.put("msg", "SUCCESS");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			rslt.put("msg", e.getMessage());
			rslt.put("cnt", cnt);
			rsltVO.setStatus(400);
			rsltVO.setResult(false);
		}
		
		rsltVO.setData(rslt);
		
		return rsltVO;
	}
	
	
	@ApiOperation(value = "청각 검사 리스트 조회"
			, notes = "청각 검사 리스트 조회"
			)
	@GetMapping(value = "getEarDiagnosisLis.do")
	public @ResponseBody Map<String, Object> getEarDiagnosisList(
			HttpServletRequest req,
			HttpServletResponse res,
			DiagnosisVO diaVO) {
		
		String user_id = jwtUtil.getLoginIdbyToken(req);
		
		diaVO.setUser_id(user_id);
		
		logger.info("■■■■■■ getEarDiagnosisList / diaVO : {}", diaVO.beanToHmap(diaVO).toString());
		List<Map<String, Object>> earList = diaDAO.getEarDiagnosisList(diaVO);
		
		Map<String, Object> list = new HashMap<String, Object>();
		
		list.put("data", earList);
		list.put("size", earList.size());
		
		return list;
	}
	
	
	@ApiOperation(value = "고막 검사 리스트 조회"
			, notes = "고막 검사 리스트 조회"
			)
	@GetMapping(value = "getDrumDiagnosisList.do")
	public @ResponseBody Map<String, Object> getDrumDiagnosisList(
			HttpServletRequest req,
			HttpServletResponse res,
//			@PathVariable String user_code,
			DiagnosisVO diaVO) {
		
		String user_id = jwtUtil.getLoginIdbyToken(req);
		
		diaVO.setUser_id(user_id);
		
		logger.info("■■■■■■ getDrumDiagnosisList / diaVO : {}", diaVO.beanToHmap(diaVO).toString());
		List<Map<String, Object>> earList = diaDAO.getDrumDiagnosisList(diaVO);
		
		Map<String, Object> list = new HashMap<String, Object>();
		
		list.put("data", earList);
		list.put("size", earList.size());
		
		return list;
	}
	
}
