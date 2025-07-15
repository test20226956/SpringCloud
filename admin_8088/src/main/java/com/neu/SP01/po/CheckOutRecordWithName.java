package com.neu.SP01.po;

public class CheckOutRecordWithName extends CheckOutRecord {
    private String customerName;  // 老人姓名
    private Integer gender;
    private Integer age;
    private String identity;     // 身份证号
    private String nurseName;
    
    
    public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
        // Calculate age when identity is set
        if (identity != null && identity.length() >= 6) {
            this.age = calculateAgeFromId(identity);
        }
    }

    private Integer calculateAgeFromId(String idCard) {
        // Implement your age calculation logic from ID card here
        // Example: Extract birth year from ID card and calculate age
        try {
            String birthYearStr = idCard.substring(6, 10);
            int birthYear = Integer.parseInt(birthYearStr);
            int currentYear = java.time.Year.now().getValue();
            return currentYear - birthYear;
        } catch (Exception e) {
            return null;
        }
    }
}