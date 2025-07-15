package com.neu.SP01.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.SP01.po.DailyNursingCount;
import com.neu.SP01.po.FloorBedStatus;

import com.neu.SP01.po.PopularNursingProject;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.service.ChartService;

@CrossOrigin("*")
@RequestMapping("/ChartController")
@RestController
public class ChartController {
	@Autowired  // 确保正确注入
    private ChartService cs;
	
	@GetMapping("/dataCount")
    public ResponseBean<Map<String, Integer>> getTodayCounts() {
        return cs.getDataCount();
    }
	
	@GetMapping("/floorBed")
    public ResponseBean<List<FloorBedStatus>> getAllFloorBedStatus() {
        return cs.getAllFloorBedStatus();
    }
	
	@GetMapping("/dailyCount")
    public ResponseBean<List<DailyNursingCount>> getDailyNursingCount(
            @RequestParam Integer days,
            @RequestParam(required = false) Integer nursingLevelId) {
        return cs.getRecentDailyNursingCount(days, nursingLevelId);
    }
	
	 @GetMapping("/popularProjects")
	    public ResponseBean<List<PopularNursingProject>> getPopularProjects(
	            @RequestParam Integer topN) {
	        return cs.getPopularProjects(topN);
	    }

}
