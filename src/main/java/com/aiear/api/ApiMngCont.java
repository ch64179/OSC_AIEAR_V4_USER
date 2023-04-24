package com.aiear.api;

import io.swagger.annotations.ApiOperation;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aiear.config.session.JwtUtil;
import com.aiear.dao.ApiMngDAO;
import com.aiear.dao.CommonDAO;
import com.aiear.util.HttpUrlUtil;
import com.aiear.vo.CommonCdVO;
import com.aiear.vo.DiagnosisVO;
import com.aiear.vo.ResponseVO;
import com.google.gson.JsonObject;


@RestController
@RequestMapping("/api/*")
@RequiredArgsConstructor
public class ApiMngCont {
	
	/** 로그 처리용 개체 선언 */
	protected Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private ApiMngDAO apiMngDAO;

	@Autowired
	private CommonDAO commonDAO;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Value("${aiear.api.rest.url}")
	String AIEAR_API_REST_URL;
	
	@Value("${aiear.api.rest.inferences.url}")
	String AIEAR_API_REST_INFERENCES_URL;
	
	@ApiOperation(value = ""
			, notes = "추론 서버 API Inference (귀 이미지 분석)"
					+ "\n 1. img_file"
					+ "<br> 	- MultipartFile 필수값"
					+ "\n 2. channel"
					+ "<br>		- 필수값(Channel값 모를경우 '3'으로 입력)"
					+ "\n 3. ear_pos"
					+ "<br>		- 필수값(귀 위치: L(왼쪽), R(오른쪽) )")
	@PostMapping(value = "inference/{user_code}.do")
	public @ResponseBody ResponseVO inference(
			HttpServletRequest req,
			HttpServletResponse res,
			@RequestParam(value = "img_file", required = true) MultipartFile img_file,
			@RequestParam(required = true) Integer channel,
			@RequestParam(required = true) String ear_pos,
			@PathVariable String user_code) {
		
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> result = new HashMap<String, Object>();
		Integer cnt = -1;
		
		try {
			String gen_user_id = jwtUtil.getLoginIdbyToken(req);
			
			//귀 이미지파일 Base64 변환
			byte[] b_img_file = img_file.getBytes();
			byte[] base64 = Base64.encodeBase64(b_img_file);
			
			BufferedImage bufferedImage = ImageIO.read(img_file.getInputStream());
		    int width = bufferedImage.getWidth();
		    int height = bufferedImage.getHeight();
			
		    //Inference 귀 이미지 JSON 생성
		    JSONObject jsonObj = new JSONObject();
		    
		    jsonObj.put("img", new String(base64, "UTF-8"));
		    jsonObj.put("w", width);
		    jsonObj.put("h", height);
		    jsonObj.put("c", channel);
			
			String url = AIEAR_API_REST_URL;
			String method = "POST";
			
			JSONArray jsonArr = new JSONArray();
			jsonArr.add(jsonObj);
			jsonArr.add(jsonObj);
			jsonArr.add(jsonObj);
			jsonArr.add(jsonObj);
			
			result = HttpUrlUtil.getHttpBodyDataToMap(url, method, jsonObj);
			
			//Call Log 적재
			Map<String, Object> callMap = new HashMap<String, Object>();
			
			callMap.put("call_type", method);
			callMap.put("call_param", jsonObj.toString());
			callMap.put("rslt_json", result.toString());
			callMap.put("param_img", b_img_file);
			callMap.put("gen_user_id", gen_user_id);
			callMap.put("user_code", user_code);
			
			Integer apiCallLogSeq = apiMngDAO.insertApiCallLog(callMap);
			
			//success : HTTP 통신 결과
			//msg : "[FAIL] ~" 에러, 성공할 경우 없음
			//result : 결과값
			//	- Ar
			//	- Myri
			//	- Normal
			//	- Ome
			//	- Tp
			//	- Tumor
			callMap.put("ear_pos", ear_pos);
			callMap.put("api_call_log_seq", apiCallLogSeq);
			callMap.put("rslt_json", result.get("result"));
			
			logger.info("■■■■■■ 통신결과 : {}", result.toString());
			
			Boolean aiInferRslt = false;
			
			//통신 결과 여부 분기점
			if((boolean) result.get("success")){
				//AI 추론 결과 분기점
				if("".equals(result.get("msg"))){
					aiInferRslt = true;
				} else {
					aiInferRslt = false;
				}
			} else {
				aiInferRslt = false;
			}
			
			// AI 추론 귀검사 결과 저장
			if(aiInferRslt){
				LinkedHashMap<String, Object> rsltMap = (LinkedHashMap<String, Object>) result.get("result");
				
				 // Max
		        Entry<String, Object> maxEntry = null;
		 
		        // Iterator
		        Set<Entry<String, Object>> entrySet = rsltMap.entrySet();
		        for(Entry<String, Object> entry : entrySet) {
		            if (maxEntry == null || (Double) entry.getValue() > (Double) maxEntry.getValue()) {
		                maxEntry = entry;
		            }
		        }
		        
		        callMap.put("result_code", maxEntry.getKey());
		        
				cnt = apiMngDAO.insertInferenceInfo(callMap);
				
				CommonCdVO cdVO = new CommonCdVO();
				
				cdVO.setCat_cd("INFERENCE_RSLT");
				cdVO.setCd(maxEntry.getKey().toUpperCase());
				
				List<CommonCdVO> msgCdVO = commonDAO.getCommonCodeList(cdVO);
				
				result.put("result_type", maxEntry.getKey().toUpperCase());
				result.put("msg", msgCdVO.get(0).getCd_val());
				
				rsltVO.setResult(true);
			} else {
				rsltVO.setMessage("AI 추론 서비스 통신 실패");
				rsltVO.setStatus(400);
				res.setStatus(400);
			}
			
		logger.info(">>>>>> result : {}", result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("msg", e.getMessage());
			result.put("cnt", cnt);
			rsltVO.setMessage("AI 추론 서비스 통신 실패");
			rsltVO.setStatus(400);
			res.setStatus(400);
		}
		
		rsltVO.setData(result);
		
		return rsltVO;
	}
	
	
	
	@ApiOperation(value = ""
			, notes = "추론 서버 API Inference (귀 이미지 분석)"
					+ "\n 1. img_files"
					+ "<br> 	- List<MultipartFile> 필수값"
					+ "\n 2. channel"
					+ "<br>		- 필수값(Channel값 모를경우 '3'으로 입력)"
					+ "\n 3. ear_pos"
					+ "<br>		- 필수값(귀 위치: L(왼쪽), R(오른쪽) )")
	@PostMapping(value = "inferences/{user_code}.do")
	public @ResponseBody ResponseVO inferences(
			HttpServletRequest req,
			HttpServletResponse res,
			@RequestParam(value = "img_files", required = true) List<MultipartFile> img_files,
			@RequestParam(required = true) Integer channel,
			@RequestParam(required = true) String ear_pos,
			@PathVariable String user_code) {
		
		ResponseVO rsltVO = new ResponseVO();
		Map<String, Object> result = new HashMap<String, Object>();
		Integer cnt = -1;
		
		try {
			String gen_user_id = jwtUtil.getLoginIdbyToken(req);
			
			JSONArray jsonArr = new JSONArray();
			byte[] param_img = img_files.get(0).getBytes();
			
			//귀 이미지파일 Base64 변환
			for(MultipartFile img_file : img_files){
				
				byte[] b_img_file = img_file.getBytes();
				byte[] base64 = Base64.encodeBase64(b_img_file);
				
				BufferedImage bufferedImage = ImageIO.read(img_file.getInputStream());
			    int width = bufferedImage.getWidth();
			    int height = bufferedImage.getHeight();
				
			    //Inference 귀 이미지 JSON 생성
			    JSONObject jsonObj = new JSONObject();
			    
			    jsonObj.put("img", new String(base64, "UTF-8"));
			    jsonObj.put("w", width);
			    jsonObj.put("h", height);
			    jsonObj.put("c", channel);
				
				
				jsonArr.add(jsonObj);
			}
			
			String url = AIEAR_API_REST_INFERENCES_URL;
			String method = "POST";
			
			result = HttpUrlUtil.getHttpBodyDataToMapJsonArr(url, method, jsonArr);
			
			//Call Log 적재
			Map<String, Object> callMap = new HashMap<String, Object>();
			
			callMap.put("call_type", method);
			callMap.put("call_param", jsonArr.toString());
			callMap.put("rslt_json", result.toString());
			callMap.put("param_img", param_img);
			callMap.put("gen_user_id", gen_user_id);
			callMap.put("user_code", user_code);
			
			Integer apiCallLogSeq = apiMngDAO.insertApiCallLog(callMap);
			
			//success : HTTP 통신 결과
			//msg : "[FAIL] ~" 에러, 성공할 경우 없음
			//result : 결과값
			//	- Ar
			//	- Myri
			//	- Normal
			//	- Ome
			//	- Tp
			//	- Tumor
			callMap.put("ear_pos", ear_pos);
			callMap.put("api_call_log_seq", apiCallLogSeq);
			callMap.put("rslt_json", result.get("result"));
			
			logger.info("■■■■■■ 통신결과 : {}", result.toString());
			
			Boolean aiInferRslt = false;
			
			//통신 결과 여부 분기점
			if((boolean) result.get("success")){
				//AI 추론 결과 분기점
				if("".equals(result.get("msg"))){
					aiInferRslt = true;
				} else {
					aiInferRslt = false;
				}
			} else {
				aiInferRslt = false;
			}
			
			// AI 추론 귀검사 결과 저장
			if(aiInferRslt){
				LinkedHashMap<String, Object> rsltMap = (LinkedHashMap<String, Object>) result.get("result");
				
				 // Max
		        Entry<String, Object> maxEntry = null;
		 
		        // Iterator
		        Set<Entry<String, Object>> entrySet = rsltMap.entrySet();
		        for(Entry<String, Object> entry : entrySet) {
		            if (maxEntry == null || (Double) entry.getValue() > (Double) maxEntry.getValue()) {
		                maxEntry = entry;
		            }
		        }
		        
		        callMap.put("result_code", maxEntry.getKey());
		        
				cnt = apiMngDAO.insertInferenceInfo(callMap);
				
				CommonCdVO cdVO = new CommonCdVO();
				
				cdVO.setCat_cd("INFERENCE_RSLT");
				cdVO.setCd(maxEntry.getKey().toUpperCase());
				
				List<CommonCdVO> msgCdVO = commonDAO.getCommonCodeList(cdVO);
				
				result.put("result_type", maxEntry.getKey().toUpperCase());
				result.put("msg", msgCdVO.get(0).getCd_val());
				
				rsltVO.setResult(true);
			} else {
				rsltVO.setMessage("AI 추론 서비스 통신 실패");
				rsltVO.setStatus(400);
				res.setStatus(400);
			}
			
		logger.info(">>>>>> result : {}", result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("msg", e.getMessage());
			result.put("cnt", cnt);
			rsltVO.setMessage("AI 추론 서비스 통신 실패");
			rsltVO.setStatus(400);
			res.setStatus(400);
		}
		
		rsltVO.setData(result);
		
		return rsltVO;
	}
	
}
