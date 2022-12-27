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
public class SMTPVO {
	
	@ApiModelProperty(
			name = "SMTPVO",
			example = "SMTPVO"
	)
	
	@ApiParam(value = "이메일 제목")
	private String subject = "[AIEAR] 임시 비밀번호 안내 이메일입니다.";
	
	@ApiParam(value = "이메일 내용")
	private String text;
	
	@ApiParam(value = "송신 이메일 주소")
	private String to_email_addr;
	
	@ApiParam(value = "수신 이메일 주소")
	private String from_email_addr;
	
	@ApiParam(value = "SMTP 전송 결과")
	private String smtp_rslt;
	
	public HashMap<String, Object> beanToHmap(SMTPVO vo) {
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
