package com.neu.SP01.po;

/**
 * 对应护工模块->健康管家 日常护理 显示老人对应的护理服务
 */
public class NursingServiceDailyDTO {
    private Integer nursingServiceId;
    private Integer customerId;//老人id,这个其实前端也有，但是也加上吧
    private String name;//服务名称，其实就是项目名称
    private Integer amount;
    private String state;//根据数量显示该服务状态
    private String timeStatus;//到期时间
    private String description;//项目描述
    private String endTime;//到期时间

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "NursingServiceDailyDTO{" +
                "nursingServiceId=" + nursingServiceId +
                ", customerId=" + customerId +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", state='" + state + '\'' +
                ", timeStatus='" + timeStatus + '\'' +
                ", description='" + description + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }

    public NursingServiceDailyDTO(Integer nursingServiceId, Integer customerId, String name, Integer amount, String state, String timeStatus, String description, String endTime) {
        this.nursingServiceId = nursingServiceId;
        this.customerId = customerId;
        this.name = name;
        this.amount = amount;
        this.state = state;
        this.timeStatus = timeStatus;
        this.description = description;
        this.endTime = endTime;
    }

    public Integer getNursingServiceId() {
        return nursingServiceId;
    }

    public void setNursingServiceId(Integer nursingServiceId) {
        this.nursingServiceId = nursingServiceId;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTimeStatus() {
        return timeStatus;
    }

    public void setTimeStatus(String timeStatus) {
        this.timeStatus = timeStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NursingServiceDailyDTO(Integer nursingServiceId, Integer customerId, String name, Integer amount, String state, String timeStatus, String description) {
        this.nursingServiceId = nursingServiceId;
        this.customerId = customerId;
        this.name = name;
        this.amount = amount;
        this.state = state;
        this.timeStatus = timeStatus;
        this.description = description;
    }

    public NursingServiceDailyDTO() {
    }
}
