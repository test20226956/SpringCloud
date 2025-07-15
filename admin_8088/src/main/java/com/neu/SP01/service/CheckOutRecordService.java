package com.neu.SP01.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neu.SP01.dao.BedDao;
import com.neu.SP01.dao.BedRecordDao;
import com.neu.SP01.dao.CheckInRecordDao;
import com.neu.SP01.dao.CheckOutRecordDao;
import com.neu.SP01.dao.CustomerDietDao;
import com.neu.SP01.dao.FamilyDao;
import com.neu.SP01.dao.NursingRecordDao;
import com.neu.SP01.dao.NursingServiceDao;
import com.neu.SP01.po.CheckOutDetailDTO;
import com.neu.SP01.po.CheckOutRecord;
import com.neu.SP01.po.CheckOutRecordWithName;
import com.neu.SP01.po.CustCheckOutDTO;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;
@Service
@Transactional(rollbackFor = Exception.class) // 添加事务注解，任何异常都会回滚
public class CheckOutRecordService {
    
	@Autowired
    private CheckOutRecordDao cord;
	@Autowired
    private CheckInRecordDao cird;
	@Autowired
    private BedRecordDao brd;
	@Autowired
    private BedDao bd;
	@Autowired
    private CustomerDietDao cdd;
	@Autowired
    private NursingRecordDao nrd;
	@Autowired
    private NursingServiceDao nsd;
	@Autowired
    private FamilyDao fd;
	private static final Logger log = LoggerFactory.getLogger(CheckOutRecordService.class);

    // 展示所有退住申请（分页）
    public PageResponseBean<List<CheckOutRecordWithName>> showCheckOut(long pageNum, long pageSize) {
    	//计算偏移量
        long offset = (pageNum - 1) * pageSize;
        
        // 查询分页数据（现在返回带姓名的结果）
        List<CheckOutRecordWithName> list = cord.showCheckOut(offset, pageSize);
        
        // 查询总数
        long total = cord.countCheckOut();
        
        // 构建分页响应
        PageResponseBean<List<CheckOutRecordWithName>> response = new PageResponseBean<>();
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
    public PageResponseBean<List<CheckOutRecordWithName>> queryCheckOut(
            String customerName, 
            Integer state,
            String applyTime,
            long pageNum, 
            long pageSize) {
        
        long offset = (pageNum - 1) * pageSize;
        List<CheckOutRecordWithName> list = cord.queryByConditions(
                customerName, state,applyTime, offset, pageSize);
        long total = cord.countByConditions(customerName, state,applyTime);
        
        PageResponseBean<List<CheckOutRecordWithName>> response = new PageResponseBean<>();
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
    
    /*
    public ResponseBean<Integer> approveCheckOut(Integer checkOutRecordId, Integer state, Integer adminId) {
        try {
            // 1. 参数校验
            if (checkOutRecordId == null || state == null || adminId == null) {
                return new ResponseBean<>(500, "参数不完整", null);
            }
            
            // 2. 获取当前日期
            String currentDate = LocalDate.now().toString();
            
            // 3. 执行审批操作
            int result = cord.approveCheckOut(checkOutRecordId, state, adminId, currentDate);
            
            if (result == 0) {
                return new ResponseBean<>(500, "未找到该退住申请", null);
            }
            
            // 4. 如果审批通过(state=1)，处理级联状态
            if (state == 1) {
                // 4.1 获取客户ID
                Integer customerId = cord.findCustomerIdByCheckOutId(checkOutRecordId);
                if (customerId == null) {
                    throw new RuntimeException("未找到关联客户");
                }
                
                
                // 4.2 获取当前有效入住记录
                Integer checkInRecordId = cird.findActiveCheckInId(customerId);
                if (checkInRecordId != null) {
                    // 4.3 更新入住记录状态为0(退住)
                    cird.updateCheckInToInactive(checkInRecordId);
                    // 更新入住记录的结束时间
                    cird.updateEndTime(checkInRecordId, currentDate);
                    
                    // 4.4 获取关联床位记录
                    Integer bedRecordId = cird.findActiveBedRecordId(checkInRecordId);
                    if (bedRecordId != null) {
                        // 4.5 更新床位记录状态为0(可用)
                        brd.hideBedRecord(bedRecordId);
                        // 更新床位记录的结束时间
                        brd.updateBedEndTime(bedRecordId, currentDate);
                        
                        // 4.6 更新实际床位状态为可用
                        bd.updatePhysicalBedStatus(bedRecordId);
                    }else {
                    	throw new RuntimeException("未找到相关床位记录");
                    }
                }else {
                	throw new RuntimeException("未找到相关入住记录");
                }
                
              //删除膳食配置
                Integer diet=cdd.deleteCustDiet(customerId);
                if(diet==null) {
                	throw new RuntimeException("未找到相关膳食配置记录");
                }
                //删除护理记录
                Integer nursingRecord=nrd.deleteOutNursingRecord(customerId);
                if(nursingRecord==null) {
                	throw new RuntimeException("未找到相关护理记录");
                }
                
              //删除护理服务
                Integer nursingService=nrd.deleteOutNursingRecord(customerId);
                if(nursingService==null) {
                	throw new RuntimeException("未找到相关护理服务");
                }
                
            }
            
            return new ResponseBean<>(200, "审批成功", result);
            
        } catch (Exception e) {
            log.error("退住审批失败，已回滚所有操作，退住记录ID：{}", checkOutRecordId, e);
            return new ResponseBean<>(500, "审批失败: " + e.getMessage(), null);
        }
    }*/
    //审批
    public ResponseBean<Integer> approveCheckOut(Integer checkOutRecordId, Integer state, Integer adminId) {
        try {
            // 1. 参数校验 - 使用明确的异常类型
            if (checkOutRecordId == null || state == null || adminId == null) {
                throw new IllegalArgumentException("参数不完整");
            }
            
            // 2. 获取当前日期
            String currentDate = LocalDate.now().toString();
            
            // 3. 执行审批操作
            int result = cord.approveCheckOut(checkOutRecordId, state, adminId, currentDate);
            
            if (result == 0) {
                throw new RuntimeException("未找到该退住申请");
            }
            
            // 4. 如果审批通过(state=1)，处理级联状态
            if (state == 1) {
                processApprovedCheckOut(checkOutRecordId, currentDate);
            }
            
            return new ResponseBean<>(200, "审批成功", result);
            
        } catch (Exception e) {
            log.error("退住审批失败，已回滚所有操作，退住记录ID：{}", checkOutRecordId, e);
            return new ResponseBean<>(500, "审批失败: " + e.getMessage(), null);
        }
    }
    protected void processApprovedCheckOut(Integer checkOutRecordId, String currentDate) {
        // 4.1 获取客户ID
        Integer customerId = cord.findCustomerIdByCheckOutId(checkOutRecordId);
        if (customerId == null) {
            throw new RuntimeException("未找到关联客户");
        }
        
        // 4.2 获取当前有效入住记录
        Integer checkInRecordId = cird.findActiveCheckInId(customerId);
        if (checkInRecordId == null) {
            throw new RuntimeException("未找到相关入住记录");
        }
        
        // 4.3 更新入住记录状态为0(退住)
        cird.updateCheckInToInactive(checkInRecordId);
        // 更新入住记录的结束时间
        cird.updateEndTime(checkInRecordId, currentDate);
        
        // 4.4 获取关联床位记录
        Integer bedRecordId = cird.findActiveBedRecordId(checkInRecordId);
        if (bedRecordId == null) {
            throw new RuntimeException("未找到相关床位记录");
        }
        
        // 4.5 更新床位记录状态为0(可用)
        brd.hideBedRecord(bedRecordId);
        // 更新床位记录的结束时间
        brd.updateBedEndTime(bedRecordId, currentDate);
        
        // 4.6 更新实际床位状态为可用
        bd.updatePhysicalBedStatus(bedRecordId);
        
        // 删除膳食配置 - 如果没有记录也不报错
        try {
            cdd.deleteCustDiet(customerId);
        } catch (Exception e) {
            log.warn("删除客户ID[{}]的膳食配置时出现警告: {}", customerId, e.getMessage());
        }
        
        // 删除护理记录 - 如果没有记录也不报错
        try {
            nrd.deleteOutNursingRecord(customerId);
        } catch (Exception e) {
            log.warn("删除客户ID[{}]的护理记录时出现警告: {}", customerId, e.getMessage());
        }
        
        // 删除护理服务 - 如果没有记录也不报错
        try {
            nsd.detectService(customerId);
        } catch (Exception e) {
            log.warn("删除客户ID[{}]的护理服务时出现警告: {}", customerId, e.getMessage());
        }
        try {
            fd.deleteFamily(customerId);
        } catch (Exception e) {
            log.warn("删除客户ID[{}]的护理记录时出现警告: {}", customerId, e.getMessage());
        }
    }
    /*=======护工系统客户管理展示用户所有的退住申请=========*/
    public List<CustCheckOutDTO> findCheckOutRecordByCustomerId(Integer pageNum, Integer pageSize, Integer customerId){
       /* //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //执行查询
        List<CustCheckOutDTO> cords = cord.findCheckOutRecordByCustomerId(customerId);
        // 3. 获取分页信息
        Page<CustCheckOutDTO> p =(Page<CustCheckOutDTO>)cords;
        // 4. 构建响应对象
        PageResponseBean<List<CustCheckOutDTO>> response = new PageResponseBean<>();
        if(p.getTotal()!=0){
            response.setStatus(200); // 成功状态码
            response.setMsg("查询成功"); // 成功消息
            response.setData(p.getResult()); // 当前页数据
            response.setTotal(p.getTotal()); // 总记录数
        }else{
            response.setStatus(500); // 成功状态码
            response.setMsg("无数据"); // 成功消息
            response.setData(p.getResult()); // 当前页数据
            response.setTotal(p.getTotal()); // 总记录数
        }
        return response;*/
        List<CustCheckOutDTO> cords = cord.findCheckOutRecordByCustomerId(customerId);
        return cords;
    }
    //护工为老人申请退住（添加退住记录）
    public boolean InsertCheckOutRecord(CheckOutRecord cor){
        cord.InsertCheckOutRecord(cor);
        return true;
    }

    //退住详细信息
    public ResponseBean<CheckOutDetailDTO> getCheckOutDetail(Integer checkOutRecordId) {
        CheckOutDetailDTO detail = cord.getCheckOutDetailById(checkOutRecordId);

        if (detail == null) {
            return new ResponseBean<>(500, "未找到有效的退住记录或入住信息");
        }

        // 计算年龄（已在setIdentity中自动计算）
        // 清除敏感信息（不返回身份证号给前端）
        detail.setIdentity(null);

        return new ResponseBean<>(200,"查询成功",detail);
    }

}