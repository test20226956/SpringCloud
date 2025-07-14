package com.neu.SP01.po;

public class NursingRecord {
	private String time;
	private Integer nursingRecordId;
	private Integer nursingServiceId;
	private Integer customerId;
	private Integer count;
	private Integer userId;
	private Integer state;//状态 1->有效护理记录

	public NursingRecord(String time, Integer nursingRecordId, Integer nursingServiceId, Integer customerId, Integer count, Integer userId, Integer state) {
		this.time = time;
		this.nursingRecordId = nursingRecordId;
		this.nursingServiceId = nursingServiceId;
		this.customerId = customerId;
		this.count = count;
		this.userId = userId;
		this.state = state;
	}

	@Override
	public String toString() {
		return "NursingRecord{" +
				"time='" + time + '\'' +
				", nursingRecordId=" + nursingRecordId +
				", nursingServiceId=" + nursingServiceId +
				", customerId=" + customerId +
				", count=" + count +
				", userId=" + userId +
				", state=" + state +
				'}';
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getNursingRecordId() {
		return nursingRecordId;
	}
	public void setNursingRecordId(Integer nursingRecordId) {
		this.nursingRecordId = nursingRecordId;
	}
	public Integer getNursingServiceId() {
		return nursingServiceId;
	}
	public void setNursingServiceId(Integer nursingServiceId) {
		this.nursingServiceId = nursingServiceId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public NursingRecord() {
		super();
	}
	
	
}
