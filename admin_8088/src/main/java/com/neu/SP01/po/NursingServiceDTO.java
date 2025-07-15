package com.neu.SP01.po;

public class NursingServiceDTO{
    private NursingProject nursingProject;

    public NursingProject getNursingProject() {
        return nursingProject;
    }

    public void setNursingProject(NursingProject nursingProject) {
        this.nursingProject = nursingProject;
    }

    public NursingService getNursingService() {
        return nursingService;
    }

    public void setNursingService(NursingService nursingService) {
        this.nursingService = nursingService;
    }

    //    private Integer nursingProjectId;
//    private Integer state;
//    private Integer time;
//    private String name;
//    private float price;
//    private String period;
//    private String description;
    private NursingService nursingService;
}