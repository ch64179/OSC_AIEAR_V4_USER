package com.aiear.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;

import lombok.Data;

@Data
public class SMSVO {
	
	@ApiModelProperty(
			name = "SMSVO",
			example = "SMSVO"
	)
	
	@ApiParam(value = "API 키")
	private String api_key = "NCSZBI23UIO0CCCV";
	
	@ApiParam(value = "API 비밀번호")
	private String api_secret = "9QKDIYLUJCTGYHASOSENQBHS266CEY0P";
	
	@ApiParam(value = "송신 모바일 번호")
	private String to_mobile_no;
	
	@ApiParam(value = "수신 모바일 번호")
	private String from_mobile_no;
	
	@ApiParam(value = "전송 TYPE")
	private String send_type = "SMS";

	@ApiParam(value = "전송 메시지")
	private String send_msg;
	
	@ApiParam(value = "app_version")
	private String app_version = "test app 1.2";
	
	@ApiParam(value = "SMS 전송 결과")
	private String sms_rslt;
	
	public HashMap<String, Object> beanToHmap(SMSVO vo) {
		HashMap<String, Object> beanAsMap = new HashMap<String, Object>();
		
		try {
			BeanInfo info = Introspector.getBeanInfo(vo.getClass());
			
			for(PropertyDescriptor fb : info.getPropertyDescriptors()){
				Method reader = fb.getReadMethod();
				//TODO: class는 제외
				if(reader != null && !"class".equals(fb.getName())){
					beanAsMap.put(fb.getName(), reader.invoke(vo));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return beanAsMap;
	}
	
}
