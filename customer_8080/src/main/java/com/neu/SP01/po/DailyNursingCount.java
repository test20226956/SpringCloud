package com.neu.SP01.po;

public class DailyNursingCount {
	private String date;          // 日期，格式：yyyy-MM-dd
    private Integer nursingNum;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getNursingNum() {
		return nursingNum;
	}
	public void setNursingNum(Integer nursingNum) {
		this.nursingNum = nursingNum;
	}
	public DailyNursingCount() {
		super();
	}
	public DailyNursingCount(String date, Integer nursingNum) {
		super();
		this.date = date;
		this.nursingNum = nursingNum;
	}
    

}
