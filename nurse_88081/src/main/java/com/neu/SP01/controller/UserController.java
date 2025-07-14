package com.neu.SP01.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.neu.SP01.po.*;
import com.neu.SP01.service.CustomerService;
import com.neu.SP01.service.NursingRecordService;
import com.neu.SP01.service.NursingServiceService;
import com.neu.SP01.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/UserController")
@RestController
public class UserController {
	@Autowired
    private UserService us;
    @Autowired
    private CustomerService cs;
    @Autowired
    private NursingServiceService nss;
    @Autowired
    private NursingRecordService nrs;
    //登录
	@PostMapping("/login")
    public ResponseBean<?> login(String account, String password) throws JsonProcessingException {
        return us.login(account, password);
    }

    @GetMapping("/showUserCust")//显示老人的信息
    public PageResponseBean<List<CustDailyNursingDTO>> showUserCust(@RequestParam(name = "pageNum",defaultValue = "1")Integer pageNum,
                                                                    @RequestParam(name = "pageSize",defaultValue = "4")Integer pageSize, @RequestParam("userId")Integer userId){
        PageResponseBean<List<CustDailyNursingDTO>> userCust = cs.findUserCust(pageNum, pageSize, userId);
        return userCust;

    }
    

    @GetMapping("/searchUserCust")//根据老人姓名模糊搜索
    public PageResponseBean<List<CustDailyNursingDTO>> searchUserCust(@RequestParam(name = "pageNum",defaultValue = "1")Integer pageNum,
                                                                      @RequestParam(name = "pageSize",defaultValue = "4")Integer pageSize,@RequestParam("userId")Integer userId,@RequestParam("name")String name){
        PageResponseBean<List<CustDailyNursingDTO>> userCustByName = cs.findUserCustByName(pageNum, pageSize, userId, name);
        return userCustByName;
    }
    @GetMapping("/showCustPro")/*======对应原型护工 日常护理 显示用户的护理服务=====*/
    public PageResponseBean<List<NursingServiceDailyDTO>> showCustPro(@RequestParam(name = "pageNum",defaultValue = "1")Integer pageNum,
                                                                      @RequestParam(name = "pageSize",defaultValue = "4")Integer pageSize, @RequestParam("customerId")Integer customerId){
        PageResponseBean<List<NursingServiceDailyDTO>> n = nss.findNursingServiceByCustomerId(pageNum, pageSize, customerId);
        return n;
    }

    @GetMapping("/searchCustPro")//对应原型护工 日常护理  按项目名字搜索用户的持有的护理服务
    public PageResponseBean<List<NursingServiceDailyDTO>> searchCustPro(@RequestParam(name = "pageNum",defaultValue = "1")Integer pageNum,
                                                                        @RequestParam(name = "pageSize",defaultValue = "4")Integer pageSize,@RequestParam("customerId")Integer customerId,@RequestParam("name")String name){
        PageResponseBean<List<NursingServiceDailyDTO>> nursingServiceByName = nss.findNursingServiceByName(pageNum, pageSize, customerId, name);
        return nursingServiceByName;
    }

    @PostMapping("/addCareRecord")//护工->添加客户护理记录
    public ResponseBean<Integer> addCareRecord(@RequestBody NursingRecord nursingRecord){

        if(nrs.addNursingRecord(nursingRecord)){
            return new ResponseBean<>(null);
        }else{
            return new ResponseBean<>(500,"添加失败");
        }
    }

    @GetMapping("/showCareRecord")//展示老人所有的护理记录（护工 健康管家 护理记录）
    public ResponseBean<List<CustNursingRecordDTO>> shoeCareRecord(@RequestParam("customerId")Integer customerId){
        List<CustNursingRecordDTO> cnrd = nrs.findByCustomerId(customerId);
        if(!cnrd.isEmpty()){
            return new ResponseBean<>(200,"查询成功",cnrd);
        }else {
            return new ResponseBean<>(500,"该老人暂无护理记录");
        }
    }
    @PostMapping("/deleteCareRecord")//护理记录移除（隐藏）
    public ResponseBean<Integer> deleteCareRecord(@RequestParam("ids") List<Integer> ids){
        if(nrs.deleteByIds(ids)){
            return new ResponseBean<>(null);
        }else{
            return new ResponseBean<>(500,"删除失败");
        }
    }
    @GetMapping("/searchCareRecord")//根据记录名称护理时间动态搜索老人的护理记录（护工 健康管家 护理记录）
    public ResponseBean<List<CustNursingRecordDTO>> searchCareRecord(@RequestParam("customerId")Integer customerId,
          @RequestParam(value = "name",required = false)String name, @RequestParam(value = "time",required = false)String time){
        List<CustNursingRecordDTO> cnrd = nrs.findByNameAndTime(customerId,name,time);
        if(!cnrd.isEmpty()){
            return new ResponseBean<>(200,"查询成功",cnrd);
        }else {
            return new ResponseBean<>(500,"无符合条件的数据");
        }
    }
}
