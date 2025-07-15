package com.neu.SP01.service;

import com.neu.SP01.dao.*;
import com.neu.SP01.po.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class) // 添加事务注解，任何异常都会回滚
public class CheckOutRecordService {
    
	@Autowired
    private CheckOutRecordDao cord;
    /*=======护工系统客户管理展示用户所有的退住申请=========*/
    public List<CustCheckOutDTO> findCheckOutRecordByCustomerId(Integer customerId){
        List<CustCheckOutDTO> cords = cord.findCheckOutRecordByCustomerId(customerId);
        return cords;
    }
    //护工为老人申请退住（添加退住记录）
    public boolean InsertCheckOutRecord(CheckOutRecord cor){
        cord.InsertCheckOutRecord(cor);
        return true;
    }

}