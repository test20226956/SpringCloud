package com.neu.SP01.po;


public class FloorBedStatus {
    private Integer floor;    // 楼层号
    private BedSta status; // 床位状态
    
    
    
    public FloorBedStatus() {
		super();
	}



	public Integer getFloor() {
		return floor;
	}



	public void setFloor(Integer floor) {
		this.floor = floor;
	}



	public BedSta getStatus() {
		return status;
	}



	public void setStatus(BedSta status) {
		this.status = status;
	}



	public FloorBedStatus(Integer floor, BedSta status) {
        this.floor = floor;
        this.status = status;
    }
}
