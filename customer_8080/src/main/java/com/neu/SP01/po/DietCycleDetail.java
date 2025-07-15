package com.neu.SP01.po;

public class DietCycleDetail extends DietCycle {
	private String mealName;   // 食物名称
    private String mealImg;    // 食物图片
    private Integer mealType;  // 食物类型
    private Integer mealState;
	public Integer getMealState() {
		return mealState;
	}
	public void setMealState(Integer mealState) {
		this.mealState = mealState;
	}
	public String getMealName() {
		return mealName;
	}
	public void setMealName(String mealName) {
		this.mealName = mealName;
	}
	public String getMealImg() {
		return mealImg;
	}
	public void setMealImg(String mealImg) {
		this.mealImg = mealImg;
	}
	public Integer getMealType() {
		return mealType;
	}
	public void setMealType(Integer mealType) {
		this.mealType = mealType;
	}
	public DietCycleDetail() {
		super();
	}
    
}
