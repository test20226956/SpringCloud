package com.neu.SP01.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.SP01.po.NursingRecordDTO;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.service.NursingRecordService;

@RestController
@RequestMapping("/NursingRecordController")
@CrossOrigin("*")
public class NursingRecordController {
	@Autowired
	private NursingRecordService nrs;

    //根据客户id得到护理记录
    @GetMapping("/showNursingRecord")
    public PageResponseBean<List<NursingRecordDTO>> showNursingRecord(
    		@RequestParam(required = false) Integer customerId,
    		@RequestParam(required = false) String time,
            @RequestParam(required = false) String projectName,
    		@RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        return nrs.showNursingRecord(customerId,time,projectName,pageNum,pageSize);
    }

    //删除护理记录
    @PostMapping("/deleteNursingRecord")
    public ResponseBean<Integer> deleteNursingRecord(@RequestParam Integer nursingRecordId) {
    	return nrs.deleteNursingRecord(nursingRecordId);
    }
}
