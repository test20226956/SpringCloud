package com.neu.SP01.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.neu.SP01.dao.BedDao;
import com.neu.SP01.dao.CheckInRecordDao;
import com.neu.SP01.dao.OutRecordDao;
import com.neu.SP01.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class) 
public class OutRecordService {
    @Autowired
    OutRecordDao ord;
    @Autowired
    private CheckInRecordDao cird;
	@Autowired
    private BedDao bd;
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
}