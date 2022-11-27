package com.aiear.vo;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RawDataVO {
	private	String	var_id;
	private	String	dvc_id;
	private	String	data_tm;
	private	Double	data_val;
	private	String  data_str;
	private long timestamp;
	
	private	String curr_data_val;
	private	String pre_data_val;
	
	public String getVar_id() {
		return var_id;
	}


	public void setVar_id(String var_id) {
		this.var_id = var_id;
	}


	public String getDvc_id() {
		return dvc_id;
	}


	public void setDvc_id(String dvc_id) {
		this.dvc_id = dvc_id;
	}


	public String getData_tm() {
		return data_tm;
	}


	public void setData_tm(String data_tm) {
		this.data_tm = data_tm;
	}
	
	public String getT_md() {
		return data_tm.substring(4,8);
	}


	public Double getData_val() {
		return data_val;
	}


	public void setData_val(Double data_val) {
		this.data_val = data_val;
	}

	public RawDataVO() {}
	
	public RawDataVO(String dvc_id, String var_id, String data_tm, Double data_val) throws ParseException {
		this.dvc_id = dvc_id;
		this.var_id = var_id;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = sdf.parse(data_tm);
		this.data_tm = sdf.format(date);
		this.data_val = data_val;
		this.timestamp = new Timestamp(date.getTime()).getTime();
	}

	public RawDataVO(String dvc_id, String var_id, String data_tm, String data_str) throws ParseException {
		this.dvc_id = dvc_id;
		this.var_id = var_id;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = sdf.parse(data_tm);
		this.data_tm = sdf.format(date);
		this.data_str = data_str;
		this.timestamp = new Timestamp(date.getTime()).getTime();
	}

	/**
	 * MQ에서 수신받은 원본 데이터를 문자열로 반환
	 * @return
	 */
	public String toRawString() {
		return dvc_id + "|" + data_tm + "|" + var_id + "|" + data_val + "|" + data_str;				
	}
	
	/**
	 * MQ에서 수신받은 원본 데이터를 문자열로 반환
	 * @return
	 */
	public String toString4His() {
		return dvc_id + " / " + var_id + " / " + data_tm + " / " + data_val + " / " + curr_data_val + " / " + pre_data_val;				
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RawDataVO [var_id=");
		builder.append(var_id);
		builder.append(", dvc_id=");
		builder.append(dvc_id);
		builder.append(", data_tm=");
		builder.append(data_tm);
		builder.append(", data_val=");
		builder.append(data_val);
		builder.append(", data_str=");
		builder.append(data_str);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append("]");
		return builder.toString();
	}


	public long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	public String getData_str() {
		return data_str;
	}


	public void setData_str(String data_str) {
		this.data_str = data_str;
	}


	public String getCurr_data_val() {
		return curr_data_val;
	}


	public void setCurr_data_val(String curr_data_val) {
		this.curr_data_val = curr_data_val;
	}


	public String getPre_data_val() {
		return pre_data_val;
	}


	public void setPre_data_val(String pre_data_val) {
		this.pre_data_val = pre_data_val;
	}


//	public Double getCurr_data_val() {
//		return curr_data_val;
//	}
//
//
//	public void setCurr_data_val(Double curr_data_val) {
//		this.curr_data_val = curr_data_val;
//	}
//
//
//	public Double getPre_data_val() {
//		return pre_data_val;
//	}
//
//
//	public void setPre_data_val(Double pre_data_val) {
//		this.pre_data_val = pre_data_val;
//	}
	

}
