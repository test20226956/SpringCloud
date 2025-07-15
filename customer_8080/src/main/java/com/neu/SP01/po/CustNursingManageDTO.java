package com.neu.SP01.po;

/**
 * 对应显示原型->护工系统客户管理的退住、外住申请
 */
public class CustNursingManageDTO extends CustDailyNursingDTO{
    private String identity;//身份证号
    private String checkInTime;//入住时间
    private String endTime;//合同到期时间

    public CustNursingManageDTO() {
    }

    @Override
    public String toString() {
        return "CustNursingManageDTO{" +
                "identity='" + identity + '\'' +
                ", checkInTime='" + checkInTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    public CustNursingManageDTO(Integer customerId, String name, Integer age, String gender, String bloodType, Integer floor, String roomNumber, String fName, String tel, Integer nursingLevelId, Integer bedNumber, String identity, String checkInTime, String endTime) {
        super(customerId, name, age, gender, bloodType, floor, roomNumber, fName, tel, nursingLevelId, bedNumber);
        this.identity = identity;
        this.checkInTime = checkInTime;
        this.endTime = endTime;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
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
}
