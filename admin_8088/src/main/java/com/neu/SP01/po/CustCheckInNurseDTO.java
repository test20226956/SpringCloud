package com.neu.SP01.po;

//这是入住登记页面的表格字段视图类
//不与数据库进行交互

public class CustCheckInNurseDTO {
	private CheckInRecord checkInRecord;
	private Customer customer;
	private Family family;
	private BedRecord bedRecord;
	private Bed bed;
	private Room room;
	private NursingLevel nursingLevel;
	private Integer age;
	

	public NursingLevel getNursingLevel() {
		return nursingLevel;
	}

	public void setNursingLevel(NursingLevel nursingLevel) {
		this.nursingLevel = nursingLevel;
	}

	public Bed getBed() {
		return bed;
	}

	public void setBed(Bed bed) {
		this.bed = bed;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	

	public CheckInRecord getCheckInRecord() {
		return checkInRecord;
	}

	public void setCheckInRecord(CheckInRecord checkInRecord) {
		this.checkInRecord = checkInRecord;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Family getFamily() {
		return family;
	}

	public void setFamily(Family family) {
		this.family = family;
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

	public CustCheckInNurseDTO() {
		super();
	}
	



////  入住登记id
//    private Integer checkInRecordId;
////  老人姓名
//    private String customerName;
////  客户年龄（讨论在前端还是后端计算）
//    private Integer age;
////  性别
//    private Integer gender;
////  身份证号
//    private String identity;
////  血型
//    private String bloodType;
////  客户本人的联系电话
//    private String tel;
////  联系人
//    private String familyName;
////  联系人电话
//    private String familyTel;
////  楼层
//    private String floor;
////  房间号
//    private String room;
////  床位号
//    private String bed;
////  入住时间
//    private String checkInTime;
////  合约到期时间
//    private String endTime;

//	public CustCheckInDTO() {
//		super();
//	}
}
