package com.neu.SP01.po;

public class NursingService {
	private Integer nursingServiceId;
	private Integer customerId;
	private Integer nursingLevelId;
	private Integer nursingProjectId;
	private Integer amount;
	private String purchaseTime;
	private String endTime;
	private Integer state;
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
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
	public Integer getNursingLevelId() {
		return nursingLevelId;
	}
	public void setNursingLevelId(Integer nursingLevelId) {
		this.nursingLevelId = nursingLevelId;
	}
	public Integer getNursingProjectId() {
		return nursingProjectId;
	}
	public void setNursingProjectId(Integer nursingProjectId) {
		this.nursingProjectId = nursingProjectId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getPurchaseTime() {
		return purchaseTime;
	}
	public void setPurchaseTime(String purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public NursingService() {
		super();
	}
	
	
}
