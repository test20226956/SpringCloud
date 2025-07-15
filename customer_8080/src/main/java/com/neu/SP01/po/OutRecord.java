package com.neu.SP01.po;

public class OutRecord {
	private Integer outRecordId;
	private Integer customerId;
	private String applyTime;
	private String examineTime;
	private Integer state;
	private Integer adminId;
	private String reason;
	private String outTime;
	private String expectedReturnTime;
	private Integer nurseId;
	private String actualReturnTime;
	public Integer getOutRecordId() {
		return outRecordId;
	}
	public void setOutRecordId(Integer outRecordId) {
		this.outRecordId = outRecordId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getAdminId() {
		return adminId;
	}
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	public String getExpectedReturnTime() {
		return expectedReturnTime;
	}
	public void setExpectedReturnTime(String expectedReturnTime) {
		this.expectedReturnTime = expectedReturnTime;
	}
	public Integer getNurseId() {
		return nurseId;
	}
	public void setNurseId(Integer nurseId) {
		this.nurseId = nurseId;
	}
	public String getActualReturnTime() {
		return actualReturnTime;
	}
	public void setActualReturnTime(String actualReturnTime) {
		this.actualReturnTime = actualReturnTime;
	}
	public OutRecord() {
		super();
	}
	
	
	
}
