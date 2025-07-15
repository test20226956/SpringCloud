package com.neu.SP01.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.SP01.po.CustCheckInDietDTO;
import com.neu.SP01.po.CustomerDiet;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.service.CustomerDietService;

@CrossOrigin("*")
@RequestMapping("/CustDietController")
@RestController
public class CustDietController {
	@Autowired
	private CustomerDietService cds;
    //    展示所有老人的膳食配置
    @GetMapping("/searchCustDiet")
    public PageResponseBean<List<CustCheckInDietDTO>> searchCustDiet(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String checkInTime, // 改为String类型
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize){
        return cds.searchCustDiet(name,checkInTime,pageNum,pageSize);
    }


    //    修改客户膳食配置
    @PostMapping("/editCustDiet")
    public ResponseBean<Integer> editCustDiet(@RequestBody CustomerDiet customerDiet){
    	return cds.editCustDiet(customerDiet);
    }

}
