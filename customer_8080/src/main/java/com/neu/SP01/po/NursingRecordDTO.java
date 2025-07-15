package com.neu.SP01.po;

public class NursingRecordDTO{
    //和项目有关的部分
	private  NursingProject nursingProject;
	private NursingService nursingService;
	
//    private String projectName;
//    private String description;

	public NursingService getNursingService() {
		return nursingService;
	}

	public void setNursingService(NursingService nursingService) {
		this.nursingService = nursingService;
	}

	public NursingProject getNursingProject() {
		return nursingProject;
	}

	public void setNursingProject(NursingProject nursingProject) {
		this.nursingProject = nursingProject;
	}

	public NursingRecord getNursingRecord() {
		return nursingRecord;
	}

	public void setNursingRecord(NursingRecord nursingRecord) {
		this.nursingRecord = nursingRecord;
	}

	public User getNurse() {
		return nurse;
	}

	public void setNurse(User nurse) {
		this.nurse = nurse;
	}

	//护理记录部分
	private NursingRecord nursingRecord;

    //护理人员
	private User nurse; //需要的是名字和tel
}
