package com.aiear.vo;

public class SysPolicyVO {
	private String log_cd;
	private String log_data;
	private String log_detail;
	
	public String getLog_cd() {
		return log_cd;
	}
	public void setLog_cd(String log_cd) {
		this.log_cd = log_cd;
	}
	public String getLog_data() {
		return log_data;
	}
	public void setLog_data(String log_data) {
		this.log_data = log_data;
	}
	public String getLog_detail() {
		return log_detail;
	}
	public void setLog_detail(String log_detail) {
		this.log_detail = log_detail;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SysPolicyVO [log_cd=");
		builder.append(log_cd);
		builder.append(", log_data=");
		builder.append(log_data);
		builder.append(", log_detail=");
		builder.append(log_detail);
		builder.append("]");
		return builder.toString();
	}
}
