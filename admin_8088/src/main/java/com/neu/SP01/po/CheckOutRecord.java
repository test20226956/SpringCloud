package com.neu.SP01.po;

public class CheckOutRecord {
	private Integer checkOutRecordId;
	private Integer customerId;
	private Integer state;
	private Integer type;
	private String applyTime;
	private String examineTime;
	private Integer adminId;
	private Integer nurseId;
	private String reason;
	
	public Integer getCheckOutRecordId() {
		return checkOutRecordId;
	}
	public void setCheckOutRecordId(Integer checkOutRecordId) {
		this.checkOutRecordId = checkOutRecordId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getExamineTime() {
		return examineTime;
	}
	public void setExamineTime(String examineTime) {
		this.examineTime = examineTime;
	}
	public Integer getAdminId() {
		return adminId;
	}
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}
	public Integer getNurseId() {
		return nurseId;
	}
	public void setNurseId(Integer nurseId) {
		this.nurseId = nurseId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public CheckOutRecord() {
		super();
	}
	
	
}
