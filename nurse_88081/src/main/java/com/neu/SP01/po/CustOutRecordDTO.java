package com.neu.SP01.po;

/**
 * 对应原型护工模块 外出申请 显示用户的外出申请记录
 */
public class CustOutRecordDTO {
    private Integer outRecordId;
    private String name;//老人姓名
    private String outTime;//外出时间
    private String expectedReturnTime;//预计回院时间
    private String reason;//申请原因
    private String checkName;//审核人姓名
    private String examineTime;//审核时间

    @Override
    public String toString() {
        return "CustOutRecordDTO{" +
                "outRecordId=" + outRecordId +
                ", name='" + name + '\'' +
                ", outTime='" + outTime + '\'' +
                ", expectedReturnTime='" + expectedReturnTime + '\'' +
                ", reason='" + reason + '\'' +
                ", checkName='" + checkName + '\'' +
                ", examineTime='" + examineTime + '\'' +
                ", state='" + state + '\'' +
                ", actualReturnTime='" + actualReturnTime + '\'' +
                '}';
    }

    public Integer getOutRecordId() {
        return outRecordId;
    }

    public void setOutRecordId(Integer outRecordId) {
        this.outRecordId = outRecordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getExpectedReturnTime() {
        return expectedReturnTime;
    }

    public void setExpectedReturnTime(String expectedReturnTime) {
        this.expectedReturnTime = expectedReturnTime;
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

    public String getActualReturnTime() {
        return actualReturnTime;
    }

    public void setActualReturnTime(String actualReturnTime) {
        this.actualReturnTime = actualReturnTime;
    }

    public CustOutRecordDTO(Integer outRecordId, String name, String outTime, String expectedReturnTime, String reason, String checkName, String examineTime, String state, String actualReturnTime) {
        this.outRecordId = outRecordId;
        this.name = name;
        this.outTime = outTime;
        this.expectedReturnTime = expectedReturnTime;
        this.reason = reason;
        this.checkName = checkName;
        this.examineTime = examineTime;
        this.state = state;
        this.actualReturnTime = actualReturnTime;
    }

    public CustOutRecordDTO() {
    }

    private String state;//审核状态
    private String actualReturnTime;//实际回院时间
}
