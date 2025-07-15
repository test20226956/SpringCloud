package com.neu.SP01.po;
// 这是客户膳食安排前端表格所需的字段视图类
public class CustDietDTO {
    private CustomerDiet customerDiet;
    private Customer customer;
    private BedRecord bedRecord;

    private Integer age;

    public CustomerDiet getCustomerDiet() {
        return customerDiet;
    }

    public void setCustomerDiet(CustomerDiet customerDiet) {
        this.customerDiet = customerDiet;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BedRecord getBedRecord() {
        return bedRecord;
    }

    public void setBedRecord(BedRecord bedRecord) {
        this.bedRecord = bedRecord;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }



//    //  客户膳食配置id
//    private Integer customerDietId;
//    //  客户姓名
//    private String customerName;
//    //  客户年龄
//    private Integer age;
//    //  楼层信息
//    private String bedInfo;
//    //  口味
//    private String flavor;
//    //  忌口
//    private String restraint;
//    //  备注
//    private String comment;

}
