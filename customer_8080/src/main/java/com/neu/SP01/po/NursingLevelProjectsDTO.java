package com.neu.SP01.po;

import java.util.List;

public class NursingLevelProjectsDTO {
    private Integer nursingLevelId;
    private List<Integer> projectIds;
    
    // getters and setters
    public Integer getNursingLevelId() {
        return nursingLevelId;
    }
    
    public void setNursingLevelId(Integer nursingLevelId) {
        this.nursingLevelId = nursingLevelId;
    }
    
    public List<Integer> getProjectIds() {
        return projectIds;
    }
    
    public void setProjectIds(List<Integer> projectIds) {
        this.projectIds = projectIds;
    }
}
