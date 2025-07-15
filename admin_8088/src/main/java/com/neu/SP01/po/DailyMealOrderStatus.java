package com.neu.SP01.po;

public class DailyMealOrderStatus {
    private boolean hasBreakfast;  // 是否点了早餐
    private boolean hasLunch;      // 是否点了午餐
    private boolean hasDinner;     // 是否点了晚餐
    
    // getters and setters
    public boolean isHasBreakfast() {
        return hasBreakfast;
    }
    public void setHasBreakfast(boolean hasBreakfast) {
        this.hasBreakfast = hasBreakfast;
    }
    public boolean isHasLunch() {
        return hasLunch;
    }
    public void setHasLunch(boolean hasLunch) {
        this.hasLunch = hasLunch;
    }
    public boolean isHasDinner() {
        return hasDinner;
    }
    public void setHasDinner(boolean hasDinner) {
        this.hasDinner = hasDinner;
    }
}
