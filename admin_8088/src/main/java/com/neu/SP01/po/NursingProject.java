package com.neu.SP01.po;

public class NursingProject {
	private Integer nursingProjectId;
	private Integer state;
	private Integer time;
	private String name;
	private String price;
	private String period;
	private String description;
	
	public Integer getNursingProjectId() {
		return nursingProjectId;
	}
	public void setNursingProjectId(Integer nursingProjectId) {
		this.nursingProjectId = nursingProjectId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public NursingProject() {
		super();
	}
	
	
}
