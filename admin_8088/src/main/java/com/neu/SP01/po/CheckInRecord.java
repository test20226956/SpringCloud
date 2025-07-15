package com.neu.SP01.po;

public class CheckInRecord {
	private Integer checkInRecordId;
	private Integer customerId;
	private Integer nursingLevelId;
	private Integer userId;
	private String checkInTime;
	private String endTime;
	private Integer state;
	public Integer getCheckInRecordId() {
		return checkInRecordId;
	}
	public void setCheckInRecordId(Integer checkInRecordId) {
		this.checkInRecordId = checkInRecordId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getNursingLevelId() {
		return nursingLevelId;
	}
	public void setNursingLevelId(Integer nursingLevelId) {
		this.nursingLevelId = nursingLevelId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getCheckInTime() {
		return checkInTime;
	}
	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public CheckInRecord() {
		super();
	}
	
	
}
