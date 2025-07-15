package com.neu.SP01.controller;

import java.util.List;

import com.neu.SP01.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.neu.SP01.service.CheckOutRecordService;
import com.neu.SP01.service.CustomerService;

@CrossOrigin("*")
@RequestMapping("/CheckOutController")
@RestController
public class CheckOutController {
	@Autowired  // 确保正确注入
    private CustomerService cs;
	@Autowired  // 确保正确注入
    private CheckOutRecordService cors;
	
	// 展示所有的退住申请列表（分页）
    @GetMapping("/showCheckOut")
    public PageResponseBean<List<CheckOutRecordWithName>> showCheckOut(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        return cors.showCheckOut(pageNum, pageSize);
    }
    //多条件查询
    @GetMapping("/searchCheckOut")
    public PageResponseBean<List<CheckOutRecordWithName>> queryCheckOut(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) Integer state,
            @RequestParam(required = false) String applyTime,
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        
        return cors.queryCheckOut(customerName, state, applyTime,pageNum, pageSize);
    }
    //审批退住
    @PostMapping("/checkCheckOut")
    public ResponseBean<Integer> approveCheckOut(
            @RequestParam Integer checkOutRecordId,
            @RequestParam Integer state,
            @RequestParam Integer adminId) {
        return cors.approveCheckOut(checkOutRecordId, state, adminId);
    }
    
    @GetMapping("/getRecord")
    public ResponseBean<CheckOutDetailDTO> getCheckOutDetail(
    		@RequestParam Integer checkOutRecordId) {
        return cors.getCheckOutDetail(checkOutRecordId);
    }
   

    @GetMapping("/showCust")//退住申请显示老人的信息
    public PageResponseBean<List<CustNursingManageDTO>> showCust(@RequestParam(defaultValue = "1")Integer pageNum,
                                                                 @RequestParam(defaultValue = "4")Integer pageSize, Integer userId){
        PageResponseBean<List<CustNursingManageDTO>> userCustManage = cs.findUserCustManage(pageNum, pageSize, userId);
        return userCustManage;
    }
    @GetMapping("/searchCust")//客户管理模块根据老人姓名模糊搜索
    public PageResponseBean<List<CustNursingManageDTO>> searchCust(@RequestParam(defaultValue = "1")Integer pageNum,
                                                                   @RequestParam(defaultValue = "4")Integer pageSize, Integer userId,String name){
        PageResponseBean<List<CustNursingManageDTO>> userCustManageByName = cs.findUserCustManageByName(pageNum, pageSize, userId, name);
        return userCustManageByName;
    }
    @PostMapping("/addCheckOutRe")//为老人添加退住记录
    public ResponseBean<Integer> addCheckOutRe(@RequestBody CheckOutRecord checkOutRecord){
        if(cors.InsertCheckOutRecord(checkOutRecord)){
            return new ResponseBean<>(null);
        }else{
            return new ResponseBean<>(500,"添加失败");
        }
    }

    @GetMapping("/showCustCheckOutRe")//展示老人所有的退住记录
    public ResponseBean<List<CustCheckOutDTO>> showCustCheckOutRe(@RequestParam(defaultValue = "1")Integer pageNum,
                                                                      @RequestParam(defaultValue = "4")Integer pageSize,Integer customerId){
        List<CustCheckOutDTO> checkOutRecordByCustomerId = cors.findCheckOutRecordByCustomerId(pageNum,pageSize,customerId);
        if(checkOutRecordByCustomerId.isEmpty()){
            return new ResponseBean<>(500,"该老人暂无退住申请记录");
        }else{
            return new ResponseBean<>(200,"查询成功",checkOutRecordByCustomerId);
        }
    }

    @GetMapping("/searchCustCheckOutRe")
    public PageResponseBean<CheckOutRecord> searchCustCheckOutRe(String cuatName){
        return null;
    }
}
