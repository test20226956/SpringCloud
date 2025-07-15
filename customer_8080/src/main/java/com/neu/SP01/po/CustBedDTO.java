package com.neu.SP01.po;

public class CustBedDTO {
    private BedRecord bedRecord;

    public BedRecord getBedRecord() {
        return bedRecord;
    }

    public void setBedRecord(BedRecord bedRecord) {
        this.bedRecord = bedRecord;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private Customer customer;
//    //  客户床位使用记录id
//    private Integer bedRecordId;
//    //  客户姓名
//    private String customerName;
//    //  客户年龄
//    private Integer age;
//    private String bedInfo;
//    private String startTime;
//    private String endTime;
}
