package com.neu.SP01.po;

public class DietCycle {
	private Integer dietCycleId;
	private String date;
	private Integer mealId;
	private Integer type;
	private Integer state;
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getDietCycleId() {
		return dietCycleId;
	}
	public void setDietCycleId(Integer dietCycleId) {
		this.dietCycleId = dietCycleId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getMealId() {
		return mealId;
	}
	public void setMealId(Integer mealId) {
		this.mealId = mealId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public DietCycle() {
		super();
	}
	
	
}
