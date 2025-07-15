package com.neu.SP01.controller;

import com.neu.SP01.po.*;
import com.neu.SP01.service.CheckOutRecordService;
import com.neu.SP01.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/CheckOutController")
@RestController
public class CheckOutController {
	@Autowired  // 确保正确注入
    private CustomerService cs;
	@Autowired  // 确保正确注入
    private CheckOutRecordService cors;
	


    @GetMapping("/showCust")//退住申请显示老人的信息
    public PageResponseBean<List<CustNursingManageDTO>> showCust(@RequestParam(name = "pageNum",defaultValue = "1")Integer pageNum,
                                                                 @RequestParam(name = "pageSize",defaultValue = "4")Integer pageSize,@RequestParam("userId") Integer userId){
        PageResponseBean<List<CustNursingManageDTO>> userCustManage = cs.findUserCustManage(pageNum, pageSize, userId);
        return userCustManage;
    }
    @GetMapping("/searchCust")//客户管理模块根据老人姓名模糊搜索
    public PageResponseBean<List<CustNursingManageDTO>> searchCust(@RequestParam(name = "pageNum",defaultValue = "1")Integer pageNum,
                                                                   @RequestParam(name = "pageSize",defaultValue = "4")Integer pageSize, @RequestParam("userId")Integer userId,@RequestParam("name")String name){
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
    public ResponseBean<List<CustCheckOutDTO>> showCustCheckOutRe(@RequestParam("customerId") Integer customerId){
        List<CustCheckOutDTO> checkOutRecordByCustomerId = cors.findCheckOutRecordByCustomerId(customerId);
        if(checkOutRecordByCustomerId.isEmpty()){
            return new ResponseBean<>(500,"该老人暂无退住申请记录");
        }else{
            return new ResponseBean<>(200,"查询成功",checkOutRecordByCustomerId);
        }
    }
}
