package com.neu.SP01.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neu.SP01.po.*;
import com.neu.SP01.service.CustomerService;
import com.neu.SP01.service.OutRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/GoOutController")
@RestController
public class OutRecordController {
    @Autowired
    OutRecordService ors;
    @Autowired
    private CustomerService cs;
    
 // 展示所有的外出退住申请列表（分页）
    @GetMapping("/showGoOut")
    public PageResponseBean<List<OutRecordWithName>> showGoOut(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        return ors.showGoOut(pageNum, pageSize);
    }
    
  //多条件查询
    @GetMapping("/searchGoOut")
    public PageResponseBean<List<OutRecordWithName>> queryGoOut(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) Integer state,
            @RequestParam(required = false) String applyTime,
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        
        return ors.queryGoOut(customerName, state, applyTime,pageNum, pageSize);
    }
  //审批外出退住
    @PostMapping("/checkGoOut")
    public ResponseBean<Integer> approveOut(
            @RequestParam Integer outRecordId,
            @RequestParam Integer state,
            @RequestParam Integer adminId) {
        return ors.approveOut(outRecordId, state, adminId);
    }

    //详情信息
    @GetMapping("/getRecord")
    public ResponseBean<OutDetailDTO> getOutDetail(
    		@RequestParam Integer outRecordId) {
        return ors.getOutDetail(outRecordId);
    }


    //客户管理模块显示老人信息（其实是增加了身份证号，入住时间以及到期时间这三项内容）
    @GetMapping("/showCust")
    public PageResponseBean<List<CustNursingManageDTO>> showCust(@RequestParam(defaultValue = "1")Integer pageNum,
                                                                 @RequestParam(defaultValue = "4")Integer pageSize, Integer userId){
        PageResponseBean<List<CustNursingManageDTO>> userCustManage = cs.findUserCustManage(pageNum, pageSize, userId);
        return userCustManage;
    }

    @PostMapping("/addGoOutRe")//护工添加老人的外出申请
    public ResponseBean<Integer> addGoOutRe(@RequestBody OutRecord outRecord){
        if(ors.addOutRecord(outRecord)){
            return new ResponseBean<>(null);
        }else{
            return new ResponseBean<>(500,"添加失败");
        }
    }

    @GetMapping("/searchCust")//客户管理模块根据老人姓名模糊搜索
    public PageResponseBean<List<CustNursingManageDTO>> searchCust(@RequestParam(defaultValue = "1")Integer pageNum,
                                                                   @RequestParam(defaultValue = "4")Integer pageSize, Integer userId,String name){
        PageResponseBean<List<CustNursingManageDTO>> userCustManageByName = cs.findUserCustManageByName(pageNum, pageSize, userId, name);
        return userCustManageByName;
    }

    @GetMapping("/showCustGoOutRe")
    public PageResponseBean<List<CustOutRecordDTO>> showCustGoOutRe(@RequestParam(defaultValue = "1")Integer pageNum,
                                                                    @RequestParam(defaultValue = "4")Integer pageSize, Integer customerId){
        PageResponseBean<List<CustOutRecordDTO>> outRecordByCustomerId = ors.findOutRecordByCustomerId(pageNum, pageSize, customerId);
        return outRecordByCustomerId;
    }

    @PostMapping("/custGoOutCome")//给老人添加回院时间
    public ResponseBean<Integer> custGoOutCome(Integer outRecordId){
        int result = ors.AddActualReturnTime(outRecordId);
        if(result == -1){
            return new ResponseBean<>(500,"外出审批未通过，不可添加回院时间");
        }else if(result == 0){
            return new ResponseBean<>(500,"已有实际回院时间，不可添加");
        }else {
            return new ResponseBean<>(200,"添加成功");
        }
    }

    @GetMapping("/searchCustGoOutRe")
    public PageResponseBean<List<CustOutRecordDTO>> searchCustGoOutRe(@RequestParam(defaultValue = "1")Integer pageNum,
                                                                      @RequestParam(defaultValue = "4")Integer pageSize, Integer customerId,String outTime){
        PageResponseBean<List<CustOutRecordDTO>> outRecordByTime = ors.findOutRecordByTime(pageNum, pageSize, customerId, outTime);
        return outRecordByTime;

    }
}
