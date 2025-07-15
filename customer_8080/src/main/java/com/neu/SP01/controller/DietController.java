package com.neu.SP01.controller;

import java.time.LocalDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.SP01.po.DietCycle;
import com.neu.SP01.po.DietCycleDetail;
import com.neu.SP01.po.Meal;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.service.DietCycleService;


@CrossOrigin("*")
@RequestMapping("/DietController")
@RestController
public class DietController {
	
	
	@Autowired
	private DietCycleService dcs;
	
    //    展示某一天的膳食安排
    @GetMapping("/showDiet")
    public ResponseBean<List<DietCycleDetail>> getByDate(
            @RequestParam String date) {
        return dcs.getByDate(date);
    }
    
//  展示某一天的膳食安排按照类型
  @GetMapping("/listDietByType")
  public ResponseBean<List<DietCycleDetail>> listDietByType(
          @RequestParam String date,@RequestParam Integer type) {
      return dcs.getByType(date,type);
  }

    //    增加一条膳食安排
    @PostMapping("/addDiet")
//    增加后可能还会进行其它操作，所以返回Diet
    public ResponseBean<Integer> addDiet(@RequestBody DietCycle dietCycle){
    	return dcs.addDiet(dietCycle);
    }

    //    删除一条膳食安排
    @PostMapping("/deleteDiet")
    public ResponseBean<Integer> deleteDiet(Integer dietCycleId){
    	return dcs.deleteDiet(dietCycleId);
    }

    //    修改膳食安排配置
    @PostMapping("/editDiet")
    public ResponseBean<Integer> editDiet(@RequestBody DietCycle dietCycle){
    	return dcs.editDiet(dietCycle);
    }

   

   
}
