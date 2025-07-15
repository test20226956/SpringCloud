package com.neu.SP01.service;

import java.time.LocalDate;
import java.util.List;

import com.neu.SP01.po.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.SP01.dao.BedDao;
import com.neu.SP01.dao.BedRecordDao;
import com.neu.SP01.dao.CheckInRecordDao;
import com.neu.SP01.dao.OutRecordDao;

import com.neu.SP01.po.CustOutRecordDTO;
import com.neu.SP01.po.OutDetailDTO;
import com.neu.SP01.po.OutRecordWithName;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@Transactional(rollbackFor = Exception.class) 
public class OutRecordService {
    @Autowired
    OutRecordDao ord;
    @Autowired
    private CheckInRecordDao cird;
	@Autowired
    private BedRecordDao brd;
	@Autowired
    private BedDao bd;
	private static final Logger log = LoggerFactory.getLogger(CheckOutRecordService.class);
    //根据老人查找对应的外出申请(外出申请功能页面中点击老人的申请记录)
    public PageResponseBean<List<CustOutRecordDTO>> findOutRecordByCustomerId(Integer pageNum,Integer pageSize,Integer customerId){
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //执行查询
        List<CustOutRecordDTO> cords = ord.findOutRecordByCustomerId(customerId);
        // 3. 获取分页信息
        Page<CustOutRecordDTO> p =(Page<CustOutRecordDTO>)cords;
        // 4. 构建响应对象
        PageResponseBean<List<CustOutRecordDTO>> response = new PageResponseBean<>();
        if(p.getTotal()!=0){
            response.setStatus(200); // 成功状态码
            response.setMsg("查询成功"); // 成功消息
            response.setData(p.getResult()); // 当前页数据
            response.setTotal(p.getTotal()); // 总记录数
        }else{
            response.setStatus(500); // 成功状态码
            response.setMsg("该老人暂无外出申请记录"); // 成功消息
            response.setData(p.getResult()); // 当前页数据
            response.setTotal(p.getTotal()); // 总记录数
        }
        return response;
    }
    //根据申请时间查询老人外出申请记录
    public PageResponseBean<List<CustOutRecordDTO>> findOutRecordByTime(Integer pageNum,Integer pageSize,Integer customerId,String outTime){
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //执行查询
        List<CustOutRecordDTO> cords = ord.findOutRecordByTime(customerId,outTime);
        // 3. 获取分页信息
        Page<CustOutRecordDTO> p =(Page<CustOutRecordDTO>)cords;
        // 4. 构建响应对象
        PageResponseBean<List<CustOutRecordDTO>> response = new PageResponseBean<>();
        if(p.getTotal()!=0){
            response.setStatus(200); // 成功状态码
            response.setMsg("查询成功"); // 成功消息
            response.setData(p.getResult()); // 当前页数据
            response.setTotal(p.getTotal()); // 总记录数
        }else{
            response.setStatus(500); // 成功状态码
            response.setMsg("无符合条件的数据"); // 成功消息
            response.setData(p.getResult()); // 当前页数据
            response.setTotal(p.getTotal()); // 总记录数
        }
        return response;
    }
    //护工添加老人的外出申请
    public boolean addOutRecord(OutRecord or){
        or.setApplyTime(LocalDate.now().toString());
        ord.addOutRecord(or);
        return true;
    }
    //给用户添加回院时间
    @Transactional
    public int AddActualReturnTime(Integer outRecordId){
        OutRecord outRecordById = ord.findOutRecordById(outRecordId);
        if(outRecordById.getState()!=1){//老人外出审批未通过，不可添加回院时间
            return -1;
        }else if(outRecordById.getActualReturnTime()==null){
            //审批通过了但是还没有回院时间才可以添加回院时间
            String actualReturnTime = LocalDate.now().toString();
            ord.AddActualReturnTime(outRecordId,actualReturnTime);
            // 4.2 获取当前有效入住记录
            Integer checkInRecordId = cird.findActiveCheckInId(outRecordById.getCustomerId());
            if (checkInRecordId != null) {
                // 4.4 获取关联床位记录
                Integer bedRecordId = cird.findActiveBedRecordId(checkInRecordId);
                if (bedRecordId != null) {

                    // 4.6 更新实际床位状态为有人
                    bd.updatePhysicalBedStatus3(bedRecordId);
                }
            }else {
                throw new RuntimeException("未找到相关入住记录");
            }
            return 1;
        }else{
            return 0;//老人外出审批通过但是已经有了回院时间
        }
    }
 // 展示所有退住申请（分页）
    public PageResponseBean<List<OutRecordWithName>> showGoOut(long pageNum, long pageSize) {
    	//计算偏移量
        long offset = (pageNum - 1) * pageSize;
        
        // 查询分页数据（现在返回带姓名的结果）
        List<OutRecordWithName> list = ord.showGoOut(offset, pageSize);
        
        // 查询总数
        long total = ord.countGoOut();
        
        // 构建分页响应
        PageResponseBean<List<OutRecordWithName>> response = new PageResponseBean<>();
        if (list == null || list.isEmpty()) {
	        response.setStatus(500);
	        response.setMsg("无符合条件的数据");
	        response.setData(null);
	        response.setTotal(0);
	    } else {
	        response.setStatus(200);
	        response.setMsg("查询成功");
	        response.setData(list);
	        response.setTotal(total);
	    }
        
        return response;
        
    }
    
  //条件查询（分页）
    public PageResponseBean<List<OutRecordWithName>> queryGoOut(
            String customerName, 
            Integer state,
            String applyTime,
            long pageNum, 
            long pageSize) {
        
        long offset = (pageNum - 1) * pageSize;
        List<OutRecordWithName> list = ord.queryByConditions(
                customerName, state,applyTime, offset, pageSize);
        long total = ord.countByConditions(customerName, state,applyTime);
        
        PageResponseBean<List<OutRecordWithName>> response = new PageResponseBean<>();
        if (list == null || list.isEmpty()) {
	        response.setStatus(500);
	        response.setMsg("无符合条件的数据");
	        response.setData(null);
	        response.setTotal(0);
	    } else {
	        response.setStatus(200);
	        response.setMsg("查询成功");
	        response.setData(list);
	        response.setTotal(total);
	    }
        
        return response;
    }
    
    //审批
    public ResponseBean<Integer> approveOut(Integer outRecordId, Integer state, Integer adminId) {
        try {
            // 1. 参数校验
            if (outRecordId == null || state == null || adminId == null) {
                return new ResponseBean<>(500, "参数不完整", null);
            }
            
            // 2. 获取当前日期
            String currentDate = LocalDate.now().toString();
            
            // 3. 执行审批操作
            int result = ord.approveOut(outRecordId, state, adminId, currentDate);
            
            if (result == 0) {
                return new ResponseBean<>(500, "未找到该外出申请", null);
            }
            
            // 4. 如果审批通过(state=1)，处理级联状态
            if (state == 1) {
                // 4.1 获取客户ID
                Integer customerId = ord.findCustomerIdByOutId(outRecordId);
                if (customerId == null) {
                    throw new RuntimeException("未找到关联客户");
                }
                
                // 4.2 获取当前有效入住记录
                Integer checkInRecordId = cird.findActiveCheckInId(customerId);
                if (checkInRecordId != null) {
                    // 4.4 获取关联床位记录
                    Integer bedRecordId = cird.findActiveBedRecordId(checkInRecordId);
                    if (bedRecordId != null) {

                        // 4.6 更新实际床位状态为可用
                        bd.updatePhysicalBedStatus2(bedRecordId);
                    }
                }else {
                	throw new RuntimeException("未找到相关入住记录");
                }
            }
            
            return new ResponseBean<>(200, "审批成功", result);
            
        } catch (Exception e) {
            log.error("外出审批失败，已回滚所有操作，外出退住记录ID：{}", outRecordId, e);
            return new ResponseBean<>(500, "审批失败: " + e.getMessage(), null);
        }
    }
    
  //退住详细信息
    public ResponseBean<OutDetailDTO> getOutDetail(Integer outRecordId) {
        OutDetailDTO detail = ord.getOutDetailById(outRecordId);
        
        if (detail == null) {
            return new ResponseBean<>(500, "未找到有效的外出记录或入住信息");
        }
        
        // 计算年龄（已在setIdentity中自动计算）
        // 清除敏感信息（不返回身份证号给前端）
        detail.setIdentity(null);
        
        return new ResponseBean<>(200,"查询成功",detail);
    }
}