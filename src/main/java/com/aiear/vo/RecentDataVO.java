package com.aiear.vo;

public class RecentDataVO {
	private long timestamp = 0;
	private RawDataVO raw = null;
	
	public RecentDataVO(RawDataVO raw) {
		this.timestamp = System.currentTimeMillis();
		this.raw = raw;
	}

	public long getTimestamp() {
		return timestamp;
	}
	public RawDataVO getRaw() {
		return raw;
	}
	public void setRaw(RawDataVO raw) {
		this.raw = raw;
		this.timestamp = System.currentTimeMillis();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecentDataVO [timestamp=");
		builder.append(timestamp);
		builder.append(", raw=");
		builder.append(raw);
		builder.append("]");
		return builder.toString();
	}

}