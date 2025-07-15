package com.neu.SP01.po;

public class EditCustRequest {
	private Customer data;
    private String endTime;
	public Customer getData() {
		return data;
	}
	public void setData(Customer data) {
		this.data = data;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public EditCustRequest() {
		super();
	}
    

}
