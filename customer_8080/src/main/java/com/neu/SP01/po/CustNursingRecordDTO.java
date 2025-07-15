package com.neu.SP01.po;

/**
 * 对应原型->护工系统 健康管家 护理记录（查看老人所有的护理记录）
 */
public class CustNursingRecordDTO {
    private Integer nursingRecordId;
    private String name;//护理记录名称（其实就对应护理项目名称）
    private String time;//护理时间

    @Override
    public String toString() {
        return "CustNursingRecordDTO{" +
                "nursingRecordId=" + nursingRecordId +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public Integer getNursingRecordId() {
        return nursingRecordId;
    }

    public void setNursingRecordId(Integer nursingRecordId) {
        this.nursingRecordId = nursingRecordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public CustNursingRecordDTO(Integer nursingRecordId, String name, String time) {
        this.nursingRecordId = nursingRecordId;
        this.name = name;
        this.time = time;
    }

    public CustNursingRecordDTO() {
    }
}
