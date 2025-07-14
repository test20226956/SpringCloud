package com.neu.SP01.po;

/**
 * 对应显示原型->护工系统健康管家的日常护理
 */
public class CustDailyNursingDTO {
    private Integer customerId;
    private String name;
    private Integer age;//老人年龄
    private String gender;
    private String bloodType;
    private Integer floor;//楼层
    private String roomNumber;//房间号
    private String fName;//联系人姓名
    private String tel;//联系方式
    private String nursingLevelId;//护理级别
    private Integer bedNumber;//床号

    @Override
    public String toString() {
        return "CustDailyNursingDTO{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", bloodType='" + bloodType + '\'' +
                ", floor=" + floor +
                ", roomNumber='" + roomNumber + '\'' +
                ", fName='" + fName + '\'' +
                ", tel='" + tel + '\'' +
                ", nursingLevelId=" + nursingLevelId +
                ", bedNumber=" + bedNumber +
                '}';
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
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

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNursingLevelId() {
        return nursingLevelId;
    }

    public void setNursingLevelId(String nursingLevelId) {
        this.nursingLevelId = nursingLevelId;
    }

    public Integer getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(Integer bedNumber) {
        this.bedNumber = bedNumber;
    }

    public CustDailyNursingDTO(Integer customerId, String name, Integer age, String gender, String bloodType, Integer floor, String roomNumber, String fName, String tel, String nursingLevelId, Integer bedNumber) {
        this.customerId = customerId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodType = bloodType;
        this.floor = floor;
        this.roomNumber = roomNumber;
        this.fName = fName;
        this.tel = tel;
        this.nursingLevelId = nursingLevelId;
        this.bedNumber = bedNumber;
    }

    public CustDailyNursingDTO() {
    }
}
