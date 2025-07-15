package com.neu.SP01.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.neu.SP01.dao.BedDao;
import com.neu.SP01.dao.BedRecordDao;
import com.neu.SP01.po.Bed;
import com.neu.SP01.po.BedRecord;
import com.neu.SP01.po.CustCheckInDTO;
import com.neu.SP01.po.Customer;
import com.neu.SP01.po.PageResponseBean;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.po.Room;

@Service

@Transactional(rollbackFor = Exception.class) // 添加事务注解，任何异常都会回滚
public class BedRecordService {
	private static final Logger log = LoggerFactory.getLogger(BedService.class);
    
    @Autowired
    private BedRecordDao brd;
    @Autowired
    private BedDao bd;
    public PageResponseBean<List<CustCheckInDTO>> queryBedUsageDetails(
            String name, 
            String startTime,
            Integer status,
            Integer pageNum,
            Integer pageSize) {
        try {
            // 参数校验与默认值
            pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
            pageSize = (pageSize == null || pageSize < 1 || pageSize > 100) ? 10 : pageSize;
            
            // 计算偏移量
            long offset = (long) (pageNum - 1) * pageSize;
            
            // 查询原始数据
            List<Map<String, Object>> rawData = brd.findBedUsageDetails(
                    name, startTime, status, offset, pageSize);
            
            // 检查查询结果是否为空
            if (rawData == null || rawData.isEmpty()) {
                return new PageResponseBean<>(500, "无符合条件的数据", null);
            }
            
            // 转换为DTO列表
            List<CustCheckInDTO> result = convertToDTOList(rawData);
            
            // 查询总数
            long total = brd.countBedUsageDetails(name, startTime, status);
            
            // 构建分页响应
            PageResponseBean<List<CustCheckInDTO>> response = 
                new PageResponseBean<>(200, "查询成功", result);
            response.setTotal(total);
            
            return response;
            
        } catch (Exception e) {
            log.error("查询床位使用详情异常", e);
            return new PageResponseBean<>(500, "查询失败: " + e.getMessage(), null);
        }
    }
    //设置DTO
	private List<CustCheckInDTO> convertToDTOList(List<Map<String, Object>> rawData) {
	    List<CustCheckInDTO> result = new ArrayList<>();
	    
	    for (Map<String, Object> item : rawData) {
	        CustCheckInDTO dto = new CustCheckInDTO();
	        
	        // 设置客户信息
	        Customer customer = new Customer();
	        customer.setCustomerId((Integer)item.get("customer_id"));
	        customer.setName((String) item.get("customer_name"));
	        customer.setGender((Integer) item.get("gender"));
	        customer.setIdentity((String) item.get("identity"));
	        dto.setCustomer(customer);
	        
	        // 设置年龄
	        dto.setAge(calculateAgeFromIdentity(customer.getIdentity()));
	        
	        // 设置床位记录 - 修改日期处理方式
	        BedRecord bedRecord = new BedRecord();
	        bedRecord.setBedRecordId((Integer) item.get("bed_record_id"));
	        
	        // 处理 start_time
	        Object startTime = item.get("start_time");
	        if (startTime != null) {
	            if (startTime instanceof java.sql.Date) {
	                bedRecord.setStartTime(((java.sql.Date) startTime).toLocalDate().toString());
	            } else if (startTime instanceof java.sql.Timestamp) {
	                bedRecord.setStartTime(((java.sql.Timestamp) startTime).toLocalDateTime().toLocalDate().toString());
	            } else {
	                bedRecord.setStartTime(startTime.toString());
	            }
	        }
	        
	        // 处理 end_time
	        Object endTime = item.get("end_time");
	        if (endTime != null) {
	            if (endTime instanceof java.sql.Date) {
	                bedRecord.setEndTime(((java.sql.Date) endTime).toLocalDate().toString());
	            } else if (endTime instanceof java.sql.Timestamp) {
	                bedRecord.setEndTime(((java.sql.Timestamp) endTime).toLocalDateTime().toLocalDate().toString());
	            } else {
	                bedRecord.setEndTime(endTime.toString());
	            }
	        }
	        
	        bedRecord.setState((Integer) item.get("state"));
	        dto.setBedRecord(bedRecord);
	        
	        // 设置房间和床位信息
	        Room room = new Room();
	        room.setBuildingNumber((Integer) item.get("building_number"));
	        room.setFloor((Integer) item.get("floor"));
	        room.setRoomNumber((String) item.get("room_number"));
	        dto.setRoom(room);
	        
	        Bed bed = new Bed();
	        bed.setBedNumber((Integer) item.get("bed_number"));
	        dto.setBed(bed);
	        
	        result.add(dto);
	    }
	    
	    return result;
	}
	//计算年龄
    private Integer calculateAgeFromIdentity(String identity) {
        if (identity == null || identity.length() < 18) {
            return null;
        }
        String birthDateStr = identity.substring(6, 14); // 提取出生日期
        try {
            LocalDate birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.BASIC_ISO_DATE);
            return Period.between(birthDate, LocalDate.now()).getYears();
        } catch (Exception e) {
            log.warn("身份证号解析失败: {}", identity, e);
            return null;
        }
    }
    
    public ResponseBean<String> updateBedEndTime(Integer bedRecordId, String endTime) {
        try {
            // 1. 参数校验
            if (bedRecordId == null || bedRecordId <= 0||endTime==null) {
                return new ResponseBean<>(500, "输入不能为空");
            }

            // 3. 检查记录是否存在
            BedRecord record = brd.findById(bedRecordId);
            if (record == null) {
                return new ResponseBean<>(500, "未找到对应的床位记录");
            }
            
            // 4. 验证结束时间是否晚于开始时间
            if (record.getStartTime() != null && 
                endTime.compareTo(record.getStartTime()) < 0) {
                return new ResponseBean<>(500, "结束时间不能早于开始时间");
            }
            
            // 5. 执行更新
            int affectedRows = brd.updateEndTime(bedRecordId, endTime);
            if (affectedRows > 0) {
                return new ResponseBean<>(200, "修改成功", null);
            } else {
                return new ResponseBean<>(500, "修改失败");
            }
            
        } catch (Exception e) {
            log.error("修改床位结束时间异常", e);
            return new ResponseBean<>(500, "系统错误: " + e.getMessage());
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    public ResponseBean<String> transferBed(
            Integer customerId, 
            Integer floor, 
            String roomNumber, 
            Integer bedNumber, 
            String endTime) {  // 结束时间由前端传入
        
        // 1. 参数校验（确保结束时间不为空）
        if (customerId == null || floor == null || roomNumber == null || 
            bedNumber == null || endTime == null) {
            return new ResponseBean<>(500, "输入不能为空", null);
        }
        
        
        // 2. 查找客户当前床位记录
        BedRecord oldRecord = brd.findActiveByCustomerId(customerId);
        if (oldRecord == null) {
            return new ResponseBean<>(500, "未找到客户当前床位信息", null);
        }
        
        // 3. 查找新床位的bedId
        Integer newBedId = bd.findBedIdByLocation(floor, roomNumber, bedNumber);
        if (newBedId == null) {
            return new ResponseBean<>(500, "未找到目标床位信息", null);
        }
        
        // 4. 检查新床位是否可用
        Integer newBedStatus = bd.getBedStatus(newBedId);
        if (newBedStatus != 0) {
            return new ResponseBean<>(500, "目标床位不可用", null);
        }
        
        // 5. 获取当前日期作为新床位的开始时间
        String currentDate = LocalDate.now().toString();
        
        try {
        	// 7. 更新旧床位状态为空闲
        	bd.updateBedStatus1(oldRecord.getBedId(), 0);
            if (bd.updateBedStatus1(oldRecord.getBedId(), 0) == 0) {
            	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                throw new RuntimeException("更新旧床位状态失败");
            }
            // 6. 更新旧床位记录 - 使用前端传入的endDate
            oldRecord.setEndTime(currentDate);  // 使用当前时间
            oldRecord.setState(0); // 设置为失效
            if (brd.updateBedRecord(oldRecord) == 0) {
            	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                throw new RuntimeException("更新旧床位记录失败");
            }
            
            
            
            // 8. 创建新床位记录（使用前端传入的endDate作为新记录的结束时间）
            BedRecord newRecord = new BedRecord();
            newRecord.setBedId(newBedId);
            newRecord.setCheckInId(oldRecord.getCheckInId());
            newRecord.setState(1); // 有效
            newRecord.setStartTime(currentDate);
            newRecord.setEndTime(endTime);  // 使用前端传入的结束时间
            if (brd.insertBedRecord1(newRecord) == 0) {
            	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                throw new RuntimeException("创建新床位记录失败");
            }
            
            // 9. 更新新床位状态为有人
            if (bd.updateBedStatus1(newBedId, 1) == 0) {
            	TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
                throw new RuntimeException("更新新床位状态失败");
            }else {
            	bd.updateBedStatus1(newBedId, 1);
            }
            
            return new ResponseBean<>(200, "床位调换成功", null);
            
        } catch (Exception e) {
            log.error("床位调换异常", e);
            return new ResponseBean<>(500, "系统异常" + e.getMessage(), null);
        }
    }
}