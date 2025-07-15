package com.neu.SP01.po;

public class BedRecord {
	private Integer bedRecordId;
	private Integer bedId;
	private Integer checkInRecordId;
	private Integer state;
	private String startTime;
	private String endTime;
	public Integer getBedRecordId() {
		return bedRecordId;
	}
	public void setBedRecordId(Integer bedRecordId) {
		this.bedRecordId = bedRecordId;
	}
	public Integer getBedId() {
		return bedId;
	}
	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}
	public Integer getCheckInId() {
		return checkInRecordId;
	}
	public void setCheckInId(Integer checkInId) {
		this.checkInRecordId = checkInId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public BedRecord() {
		super();
	}
	
}
