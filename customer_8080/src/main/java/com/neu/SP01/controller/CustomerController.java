package com.neu.SP01.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neu.SP01.po.CustCheckInDTO;
import com.neu.SP01.po.CustCheckInNurseDTO;
import com.neu.SP01.po.Customer;
import com.neu.SP01.po.CustomerWithCall;
import com.neu.SP01.po.EditCustRequest;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;

import com.neu.SP01.service.CustomerService;

@CrossOrigin("*")
@RestController
@RequestMapping("/CustomerController")
public class CustomerController {
	
	@Autowired  // 确保正确注入
    private CustomerService cs;
   
	
	// 搜索所有自理老人(分页)
    @GetMapping("/showSelfCust")
    public PageResponseBean<?> showSelfCust(@RequestParam(name = "pageNum",defaultValue = "1") long pageNum,@RequestParam(name = "pageSize",defaultValue = "10")long pageSize){
    	return cs.getSelfCareCustomersByPage(pageNum, pageSize);
    }
    
    // 搜索所有护理老人(分页)
    @GetMapping("/showCareCust")
    public PageResponseBean<?>showCareCust(@RequestParam(defaultValue = "1") long pageNum,@RequestParam(defaultValue = "10") long pageSize){
        return cs.getCareCustomersByPage(pageNum,pageSize);
    }
    // 按条件搜索老人(分页)
    @GetMapping("/searchCust")
    public PageResponseBean<?> searchCustomers(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String checkInTime, // 改为String类型
        @RequestParam(defaultValue = "1") long pageNum,
        @RequestParam(defaultValue = "10") long pageSize) {
        
        return cs.searchCustomers(type, name, checkInTime, pageNum, pageSize);
    }
    
 // 按条件搜索护理老人(分页)
    @GetMapping("/searchCareCust")
    public PageResponseBean<?> searchCareCustomers(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String checkInTime, // 改为String类型
        @RequestParam(required = false) Integer nursingLevelId,
        @RequestParam(defaultValue = "1") long pageNum,
        @RequestParam(defaultValue = "10") long pageSize) {
        
        return cs.searchCareCustomers(name, checkInTime,nursingLevelId, pageNum, pageSize);
    }
    
    // 按条件搜索没级别的护理老人(分页)
    @GetMapping("/searchNoLevelCareCust")
    public PageResponseBean<?> searchNoLevelCareCustomers(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String checkInTime,
        @RequestParam(defaultValue = "1") long pageNum,
        @RequestParam(defaultValue = "10") long pageSize) {
        
        return cs.searchNoLevelCareCustomers(name, checkInTime, pageNum, pageSize);
    }
    
    @GetMapping("/searchCustByIdentity")
    public ResponseBean<?> searchCustByIdentity( String identity) {
        
        return cs.searchCustByIdentity(identity);
    }
    
    //  添加入住登记
    
    //  编辑客户信息
    @PostMapping("/editCust")
    public ResponseBean<Integer> editCust(@RequestBody EditCustRequest re){
        return cs.editCust(re.getData(),re.getEndTime());
    }
    
  //展示没有护工的护理老人
    @GetMapping("/showUnCust")
    public PageResponseBean<List<CustCheckInNurseDTO>> showUnCust(@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10")Integer pageSize){
        return cs.showUnCust(pageNum,pageSize);
    }
    
 // 按条件搜索没有护工的护理老人(分页)
    @GetMapping("/searchUnCust")
    public PageResponseBean<?> searchUnCust(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String checkInTime, // 改为String类型
        @RequestParam(required = false) Integer nursingLevelId,
        @RequestParam(defaultValue = "1") long pageNum,
        @RequestParam(defaultValue = "10") long pageSize) {
        
        return cs.searchUnCust(name, checkInTime,nursingLevelId, pageNum, pageSize);
    }
    
//  呼叫护工
    @PostMapping("/call")
    public ResponseBean<Integer> call(@RequestParam Integer customerId ,@RequestParam String date){
        return cs.call(customerId,date);
    }
    
//  不呼叫护工
    @PostMapping("/noCall")
    public ResponseBean<Integer> noCall(@RequestParam Integer callId){
        return cs.noCall(callId);
    }
    
//  护工看
    @GetMapping("/listCall")
    public ResponseBean<Map<String, Object>> listCall(@RequestParam Integer userId){
        return cs.listCall(userId);
    }
}
