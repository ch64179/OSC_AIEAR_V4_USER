/**
 * 
 */
package com.aiear.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.aiear.vo.LoginVO;

/**
 * @packageName : com.aiear.util
 * @fileName	: SMTPUtil.java
 * @author		: pcw
 * @date		: 2022. 12. 20.
 * @description	:
 * ===============================================
 * DATE				AUTHOR			NOTE
 * 2022. 12. 20.		pcw				최초 생성
 */
@Service
public class SMTPUtil {
	
	@Autowired
    private JavaMailSender emailSender;

	
	public String smtpText(String userNm, String rndPwd) {
		
		String txt = "";
		LocalDateTime now = LocalDateTime.now();
		String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm"));
		
		txt = "안녕하세요. " + userNm + " 고객님." + "\n"
			+ "요청하신 임시 비밀번호는 다음과 같습니다." + "\n"
			+ "임시 비밀번호 : " + rndPwd  + "\n"
			+ "발급시간 : " + now.format(DateTimeFormatter.ofPattern(formatedNow)) + "\n"
			+ "로그인 하신 후 반드시 비밀번호를 변경해주세요." + "\n"
			+ "AIear 서비스를 이용해 주셔서 감사합니다." + "\n"
			+ "더욱 편리한 서비스를 제공하기 위해 항상 최선을 다하겠습니다." + "\n"
			+ "감사합니다."; 
		
		return txt;
	}
	
	
    /**
     * Description	: 
     * @method		: sendSimpleMessage
     * @author		: pcw
     * @date		: 2022. 12. 21.
     * @param to
     * @param subject
     * @param text
     * @return
     */
    public Map<String, Object> sendSimpleMessage(String to, String subject, String text) {
    	
    	Map<String, Object> rslt = new HashMap<String, Object>();
    	
    	try {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(text);
	        emailSender.send(message);
	        
	        rslt.put("result", "SUCCESS");
    	} catch (Exception e) {
    		// TODO: handle exception
    		rslt.put("result", "FAIL");
    		
    	}
    	
    	return rslt;
    }

}
