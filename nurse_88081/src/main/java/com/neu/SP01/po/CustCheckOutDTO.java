package com.neu.SP01.po;

/**
 * 对应原型护工系统 客户管理 点击查看客户的退住申请记录
 */
public class CustCheckOutDTO {
    private Integer checkOutRecordId;
    private String name;//老人姓名
    private String applyTime;//申请时间
    private String type;//申请类型
    private String reason;//申请原因
    private String checkName;//审核人姓名
    private String examineTime;//审核时间
    private String state;//申请状态

    @Override
    public String toString() {
        return "CustCheckOutDTO{" +
                "checkOutRecordId=" + checkOutRecordId +
                ", name='" + name + '\'' +
                ", applyTime='" + applyTime + '\'' +
                ", type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", checkName='" + checkName + '\'' +
                ", examineTime='" + examineTime + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public Integer getCheckOutRecordId() {
        return checkOutRecordId;
    }

    public void setCheckOutRecordId(Integer checkOutRecordId) {
        this.checkOutRecordId = checkOutRecordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getExamineTime() {
        return examineTime;
    }

    public void setExamineTime(String examineTime) {
        this.examineTime = examineTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public CustCheckOutDTO(Integer checkOutRecordId, String name, String applyTime, String type, String reason, String checkName, String examineTime, String state) {
        this.checkOutRecordId = checkOutRecordId;
        this.name = name;
        this.applyTime = applyTime;
        this.type = type;
        this.reason = reason;
        this.checkName = checkName;
        this.examineTime = examineTime;
        this.state = state;
    }

    public CustCheckOutDTO() {
    }
}
