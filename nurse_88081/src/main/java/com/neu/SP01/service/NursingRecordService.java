package com.neu.SP01.service;

import com.neu.SP01.dao.NursingRecordDao;
import com.neu.SP01.dao.NursingServiceDao;
import com.neu.SP01.po.NursingRecord;
import com.neu.SP01.po.CustNursingRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class NursingRecordService {
    @Autowired
    private NursingRecordDao nrd;
    @Autowired
    private NursingServiceDao nsd;
    //添加老人护理记录
    @Transactional
    public boolean addNursingRecord(NursingRecord nr){
        nrd.addNursingRecord(nr);// 插入护理记录
        nsd.updateNursingServiceAmount(nr.getCount(),nr.getNursingServiceId());//// 更新剩余服务次数
        return true;
    }
    //展示老人所有的护理记录（护工 健康管家 护理记录）
    public List<CustNursingRecordDTO> findByCustomerId(Integer customerId){
        List<CustNursingRecordDTO> cords = nrd.findByCustomerId(customerId);
        return cords;
    }
    //搜索护理记录（原型中体现的名称和护理时间 动态sql）
    public List<CustNursingRecordDTO> findByNameAndTime(Integer customerId,String name, String time){
        List<CustNursingRecordDTO> byNameAndTime = nrd.findByNameAndTime(customerId, name, time);
        return byNameAndTime;
    }
    //逻辑删除老人的护理记录
    public boolean deleteByIds(List<Integer> ids){
        nrd.deleteByIds(ids);
        return true;
    }

}