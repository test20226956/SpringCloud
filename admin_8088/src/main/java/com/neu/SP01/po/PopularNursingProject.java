package com.neu.SP01.po;

public class PopularNursingProject {
	private String nursingPro;  // 护理项目名称
    private Integer count;      // 购买次数
    
    
    public String getNursingPro() {
		return nursingPro;
	}

	public void setNursingPro(String nursingPro) {
		this.nursingPro = nursingPro;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	// 构造方法
    public PopularNursingProject() {}
    
    public PopularNursingProject(String nursingPro, Integer count) {
        this.nursingPro = nursingPro;
        this.count = count;
    }

}
