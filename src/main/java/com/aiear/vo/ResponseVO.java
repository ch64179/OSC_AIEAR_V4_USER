package com.aiear.vo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseVO {
	
	@ApiModelProperty(example = "응답 데이터")
	private Map<String, Object> data = new HashMap<String, Object>();
	
	@ApiModelProperty(example = "메세지")
	private String message;
	
	@ApiModelProperty(example = "상태코드")
	private int status;
	
	@ApiModelProperty(example = "시간")
	private LocalDateTime timestamp;
	
	@ApiModelProperty(example = "요청 데이터")
	private Map<String, Object> reqData = new HashMap<String, Object>();
	
	@ApiModelProperty(example = "Ajax Result")
	private Boolean result = false;
	
	public ResponseVO() {
		this(HttpStatus.OK);
	}
	
	public ResponseVO(HttpStatus httpStatus) {
		this.status = httpStatus.value();
		this.message = httpStatus.getReasonPhrase();
		this.data = new HashMap<>();
		this.timestamp = LocalDateTime.now();
		this.reqData = new HashMap<>();
	}
	
	public void add(String key, Object value) {
		this.data.put(key, value);
	}
	
}
