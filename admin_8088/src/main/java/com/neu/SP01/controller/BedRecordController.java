package com.neu.SP01.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.SP01.po.CustCheckInDTO;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.service.BedRecordService;

@CrossOrigin("*")
@RequestMapping("/BedRecordController")
@RestController
public class BedRecordController {
	@Autowired
	private BedRecordService brs;
	@GetMapping("/searchBedRecord")
    public PageResponseBean<List<CustCheckInDTO>> queryBedUsageDetails(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) Integer state,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        return brs.queryBedUsageDetails(name, startTime, state, pageNum, pageSize);
    }
	
	/*修改床位结束时间*/
    @PostMapping("/editBedRecord")
    public ResponseBean<String> updateEndTime(
    		@RequestParam Integer bedRecordId,
            @RequestParam String endTime) {
        
        return brs.updateBedEndTime(bedRecordId, endTime);
    }
    /*床位调换*/
    @PostMapping("/changeBed")
    public ResponseBean<String> transferBed(
            @RequestParam Integer customerId,
            @RequestParam Integer floor,
            @RequestParam String roomNumber,
            @RequestParam Integer bedNumber,
            @RequestParam String endTime) {
        
        return brs.transferBed(customerId, floor, roomNumber, bedNumber, endTime);
    }

}
