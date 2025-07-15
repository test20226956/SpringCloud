package com.neu.SP01.po;

/**
 * 客户端用于展示老人信息的视图实体类
 */
public class ClientCustDTO {
    private String image;
    private Integer customerId;
    private String name;
    private String address;//老人详细住址，房间号床位号等信息
    private String nursingLevel;//护理名字
    private String checkInTime;
    private String endTime;
    private String userName;
    private String tel;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNursingLevel() {
        return nursingLevel;
    }

    public void setNursingLevel(String nursingLevel) {
        this.nursingLevel = nursingLevel;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "ClientCustDTO{" +
                "image='" + image + '\'' +
                ", customerId=" + customerId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", nursingLevel='" + nursingLevel + '\'' +
                ", checkInTime='" + checkInTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", userName='" + userName + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }

    public ClientCustDTO(String image, Integer customerId, String name, String address, String nursingLevel, String checkInTime, String endTime, String userName, String tel) {
        this.image = image;
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.nursingLevel = nursingLevel;
        this.checkInTime = checkInTime;
        this.endTime = endTime;
        this.userName = userName;
        this.tel = tel;
    }

    public ClientCustDTO() {
    }
}
