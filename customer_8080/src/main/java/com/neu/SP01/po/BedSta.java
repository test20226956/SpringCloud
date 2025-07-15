package com.neu.SP01.po;

//某层床位的统计信息
public class BedSta {
    //  总床数
    private Integer total;
    //  空闲床数
    private Integer free;
    //  有人床数
    private Integer taken;
    //  外出床数
    private Integer goOut;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getFree() {
		return free;
	}
	public void setFree(Integer free) {
		this.free = free;
	}
	public Integer getTaken() {
		return taken;
	}
	public void setTaken(Integer taken) {
		this.taken = taken;
	}
	public Integer getGoOut() {
		return goOut;
	}
	public void setGoOut(Integer goOut) {
		this.goOut = goOut;
	}
	public BedSta() {
		super();
	}
    
    
}
