package com.neu.SP01.po;

public class CheckOutDetailDTO extends CheckOutRecord {
    private String customerName;  // 客户姓名
    private Integer gender;      // 性别
    private Integer age;         // 年龄（根据身份证计算）
    private Integer building;     // 楼栋
    private Integer floor;        // 楼层
    private String roomNumber;   // 房间号
    private Integer bedNumber;    // 床位号
    private String identity;     // 身份证号（不返回给前端）
    private String nurseName;
    
    
    
    public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getBuilding() {
		return building;
	}

	public void setBuilding(Integer building) {
		this.building = building;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Integer getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(Integer bedNumber) {
		this.bedNumber = bedNumber;
	}

	public String getIdentity() {
        return identity;
    }
    
    public void setIdentity(String identity) {
        this.identity = identity;
        calculateAgeFromId();
    }
    
    
    public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	// 计算年龄的方法
    private void calculateAgeFromId() {
        if (identity != null && identity.length() >= 14) {
            try {
                String birthYearStr = identity.substring(6, 10);
                int birthYear = Integer.parseInt(birthYearStr);
                int currentYear = java.time.Year.now().getValue();
                this.age = currentYear - birthYear;
            } catch (Exception e) {
                this.age = null;
            }
        }
    }
}