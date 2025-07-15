package com.neu.SP01.controller;


import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.SP01.po.CustDailyNursingDTO;
import com.neu.SP01.po.CustNursingRecordDTO;
import com.neu.SP01.po.NursingRecord;
import com.neu.SP01.po.NursingServiceDailyDTO;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.po.User;
import com.neu.SP01.service.CheckInRecordService;
import com.neu.SP01.service.CustomerService;
import com.neu.SP01.service.NursingRecordService;
import com.neu.SP01.service.NursingServiceService;
import com.neu.SP01.service.UserService;

@CrossOrigin("*")
@RequestMapping("/UserController")
@RestController
public class UserController {
	@Autowired
    private UserService us;
    @Autowired
    private CustomerService cs;
    @Autowired
    private CheckInRecordService cirs;
    @Autowired
    private NursingServiceService nss;
    @Autowired
    private NursingRecordService nrs;
    //登录
	@PostMapping("/login")
    public ResponseBean<?> login(String account,String password) throws JsonProcessingException {
        return us.login(account, password);
    }
	//展示
    @GetMapping("/showUser")
    public PageResponseBean<List<User>> getRegularUsers(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        return us.getRegularUsers(pageNum, pageSize);
    }
    //搜索
    @GetMapping("/searchUser")
    public PageResponseBean<List<User>> searchRegularUsers(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String employmentDate,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        return us.searchRegularUsers(
                userName, account, employmentDate, pageNum, pageSize);
    }

    //新增员工
    @PostMapping("/addUser")
    public ResponseBean<String> addUser(@RequestBody User user) {
        return us.addUser(user);
    }

    //更新
    @PostMapping("/editUser")
    public ResponseBean<String> updateUser(@RequestBody User user) {
        return us.updateUser(user);
    }

  //删除
    @PostMapping("/deleteUser")
    public ResponseBean<String> deleteUser(@RequestParam Integer userId) {
        return us.deleteUser(userId);
    }

    @GetMapping("/showUserCust")//这里应该新建一个DTO对应展示
    public PageResponseBean<List<CustDailyNursingDTO>> showUserCust(@RequestParam(defaultValue = "1")Integer pageNum,
                                                                    @RequestParam(defaultValue = "4")Integer pageSize,Integer userId){
        PageResponseBean<List<CustDailyNursingDTO>> userCust = cs.findUserCust(pageNum, pageSize, userId);
        return userCust;

    }
    

    @GetMapping("/searchUserCust")//根据老人姓名模糊搜索
    public PageResponseBean<List<CustDailyNursingDTO>> searchUserCust(@RequestParam(defaultValue = "1")Integer pageNum,
                                                                      @RequestParam(defaultValue = "4")Integer pageSize,Integer userId,String name){
        PageResponseBean<List<CustDailyNursingDTO>> userCustByName = cs.findUserCustByName(pageNum, pageSize, userId, name);
        return userCustByName;
    }
    //给老人添加护工
    @PostMapping("/addUserCust")
    public ResponseBean<Integer> addUserCust(Integer customerId, Integer userId){
        return cirs.addUserCust(customerId,userId);
    }
    //给老人移除护工
    @PostMapping("/deleteUserCust")
    public ResponseBean<Integer> deleteUserCust(Integer customerId, Integer userId){
    	return cirs.deleteUserCust(customerId,userId);
    }


    @GetMapping("/showCustPro")/*======对应原型护工 日常护理 显示用户的护理服务=====*/
    public PageResponseBean<List<NursingServiceDailyDTO>> showCustPro(@RequestParam(defaultValue = "1")Integer pageNum,
                                                                      @RequestParam(defaultValue = "4")Integer pageSize,Integer customerId){
        PageResponseBean<List<NursingServiceDailyDTO>> n = nss.findNursingServiceByCustomerId(pageNum, pageSize, customerId);
        return n;
    }

    @GetMapping("/searchCustPro")//对应原型护工 日常护理  按项目名字搜索用户的持有的护理服务
    public PageResponseBean<List<NursingServiceDailyDTO>> searchCustPro(@RequestParam(defaultValue = "1")Integer pageNum,
                                                          @RequestParam(defaultValue = "4")Integer pageSize,Integer customerId,String name){
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
    public ResponseBean<List<CustNursingRecordDTO>> shoeCareRecord(Integer customerId){
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
    public ResponseBean<List<CustNursingRecordDTO>> searchCareRecord(Integer customerId,String name, String time){
        List<CustNursingRecordDTO> cnrd = nrs.findByNameAndTime(customerId,name,time);
        if(!cnrd.isEmpty()){
            return new ResponseBean<>(200,"查询成功",cnrd);
        }else {
            return new ResponseBean<>(500,"无符合条件的数据");
        }
    }

    /*========================客户端护理查看=======================*/
    @GetMapping("/clientShowCustPro")//展示老人购买的所有护理服务
    public ResponseBean<List<NursingServiceDailyDTO>> clientShowCustPro(Integer customerId){
        List<NursingServiceDailyDTO> service = nss.findNursingServiceByCustomerIdOnClient(customerId);
        if(service.isEmpty()){
            return new ResponseBean<>(500,"您暂无已购买的护理服务");
        }else{
            return new ResponseBean<>(200,"查询成功",service);
        }
    }
    @GetMapping("/clientSearchCustPro")//按名字搜索老人购买的所有护理服务
    public ResponseBean<List<NursingServiceDailyDTO>> searchCustPro(Integer customerId,String name){
        List<NursingServiceDailyDTO> service = nss.findNursingServiceByNameOnClient(customerId, name);
        if(service.isEmpty()){
            return new ResponseBean<>(500,"无符合条件的数据");
        }else {
            return new ResponseBean<>(200,"查询成功",service);
        }
    }
}
