package com.aiear.util;

import java.util.HashMap;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import org.json.simple.JSONObject;

import com.aiear.vo.SMSVO;

public class SMSUtil {
	
	// 임시 전화번호
	public static final String TOMOBILENO = "01094353453";
    
	public static SMSVO sendSMS(SMSVO smsVO) {
		JSONObject obj = new JSONObject();
		
		smsVO.setTo_mobile_no(TOMOBILENO);
		
	    String api_key = smsVO.getApi_key();
	    String api_secret = smsVO.getApi_secret();
	    Message coolsms = new Message(api_key, api_secret);

	    // 4 params(to, from, type, text) are mandatory. must be filled
	    HashMap<String, String> params = new HashMap<String, String>();
	    params.put("to", TOMOBILENO);	// 수신전화번호
	    params.put("from", smsVO.getFrom_mobile_no());	// 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
	    params.put("type", smsVO.getSend_type());
	    params.put("text", smsVO.getSend_msg());
	    params.put("app_version", smsVO.getApp_version()); // application name and version

	    try {
	      obj = (JSONObject) coolsms.send(params);
	      System.out.println(obj.toString());
	      smsVO.setSms_rslt(obj.toJSONString());
	    } catch (CoolsmsException e) {
	      System.out.println(e.getMessage());
	      System.out.println(e.getCode());
	    }
	    
	    return smsVO;
	  }
    
}
