package com.aiear.vo;

public class StatVO {
	private	String	dvc_id;
	private	String	var_id;
	private	String	data_tm;

	private	Double	data_sum;
	private	Double	data_max;
	private	Double	data_min;
	private	Integer	data_cnt;
	
//	private	String t_ymd;
//	private	String t_md;
//	private	String t_mm;//월
//	private	String t_hhm;
//	private	String t_hhmm;
	
	
	public StatVO(String dvc_id, String var_id, String data_tm, Double data_val) {
		super();
		setStatVO(dvc_id, var_id, data_tm, data_val);
	}
	public StatVO(RawDataVO raw) {
		super();
		setStatVO(raw.getDvc_id(), raw.getVar_id(), raw.getData_tm(), raw.getData_val());
	}
	
	/**
	 * 내부용 : VO 값 초기화
	 * @param dvc_id
	 * @param var_id
	 * @param data_tm
	 * @param data_val
	 */
	private void setStatVO(String dvc_id, String var_id, String data_tm, Double data_val) {
		this.dvc_id = dvc_id;
		this.var_id = var_id;
		this.data_tm = data_tm;
		
		this.data_sum = data_val;
		this.data_max = data_val;
		this.data_min = data_val;
		this.data_cnt = 1;
		
//		this.t_ymd = data_tm.substring(0, 8);
//		this.t_md = data_tm.substring(4, 8);
//		this.t_mm = data_tm.substring(4, 6);
//		this.t_hhm = data_tm.substring(8, 11);
//		this.t_hhmm = data_tm.substring(8, 12);
	}

	
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
	public Double getData_sum() {
		return data_sum;
	}
	public void setData_sum(Double data_sum) {
		this.data_sum = data_sum;
	}
	public Double getData_max() {
		return data_max;
	}
	public void setData_max(Double data_max) {
		this.data_max = data_max;
	}
	public Double getData_min() {
		return data_min;
	}
	public void setData_min(Double data_min) {
		this.data_min = data_min;
	}
	public Integer getData_cnt() {
		return data_cnt;
	}
	public void setData_cnt(Integer data_cnt) {
		this.data_cnt = data_cnt;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StatVO [dvc_id=");
		builder.append(dvc_id);
		builder.append(", var_id=");
		builder.append(var_id);
		builder.append(", data_tm=");
		builder.append(data_tm);
		builder.append(", data_sum=");
		builder.append(data_sum);
		builder.append(", data_max=");
		builder.append(data_max);
		builder.append(", data_min=");
		builder.append(data_min);
		builder.append(", data_cnt=");
		builder.append(data_cnt);
		builder.append("]");
		return builder.toString();
	}

}
