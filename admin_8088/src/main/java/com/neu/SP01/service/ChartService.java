package com.neu.SP01.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date; // 注意这里是java.util.Date
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neu.SP01.dao.BedDao;
import com.neu.SP01.dao.CheckInRecordDao;
import com.neu.SP01.dao.CheckOutRecordDao;
import com.neu.SP01.dao.NursingRecordDao;
import com.neu.SP01.dao.NursingServiceDao;
import com.neu.SP01.dao.UserDao;
import com.neu.SP01.po.BedSta;
import com.neu.SP01.po.DailyNursingCount;
import com.neu.SP01.po.FloorBedStatus;
import com.neu.SP01.po.PopularNursingProject;
import com.neu.SP01.po.ResponseBean;

@Service
@Transactional(rollbackFor = Exception.class) // 添加事务注解，任何异常都会回滚
public class ChartService {
	@Autowired
    private CheckInRecordDao cird;
	@Autowired
    private CheckOutRecordDao cord;
	@Autowired
    private UserDao ud;
	@Autowired
    private BedDao bd;
	@Autowired
    private NursingRecordDao nrd;
	@Autowired
    private NursingServiceDao nsd;
	
	public ResponseBean<Map<String, Integer>> getDataCount() {
        try {
            // 获取系统当前日期并格式化为yyyy-MM-dd字符串
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = sdf.format(new Date());
            
            // 查询数据库
            int checkInToday = cird.countTodayCheckIns(todayStr);
            int checkOutToday = cord.countTodayCheckOuts(todayStr);
            int totalCust = cird.countCustomer();
            int totalNurse = ud.countNurse();
            
            // 构建返回结果
            Map<String, Integer> result = new HashMap<>();
            result.put("checkInToday", checkInToday);
            result.put("checkOutToday", checkOutToday);
            result.put("totalCust", totalCust);
            result.put("totalNurse", totalNurse);
            
            return new ResponseBean<>(200, "查询成功", result);
        } catch (Exception e) {
            return new ResponseBean<>(500, "查询失败");
        }
    }
	
	public ResponseBean<List<FloorBedStatus>> getAllFloorBedStatus() {
        try {
            List<FloorBedStatus> result = new ArrayList<>();
            
            // 查询1-7层的数据
            for (int floor = 1; floor <= 7; floor++) {
                BedSta status = bd.countBedStatusByFloor(floor);
                result.add(new FloorBedStatus(floor, status));
            }
            
            return new ResponseBean<>(200, "查询成功", result);
        } catch (Exception e) {
            return new ResponseBean<>(500, "查询失败: " + e.getMessage());
        }
    }
	
	
	public ResponseBean<List<DailyNursingCount>> getRecentDailyNursingCount(
            Integer days, Integer nursingLevelId) {
        try {
            // 参数校验
            if (days == null || days <= 0) {
                return new ResponseBean<>(500, "天数参数必须大于0");
            }
            
            // 查询数据库
            List<DailyNursingCount> result = nrd.countRecentDailyNursing(
                days, nursingLevelId);
            
            // 确保返回所有日期，即使没有记录的日子也返回0
            List<DailyNursingCount> completeResult = fillMissingDates(result, days);
            
            return new ResponseBean<>(200, "查询成功", completeResult);
        } catch (Exception e) {
            return new ResponseBean<>(500, "查询失败: " + e.getMessage());
        }
    }
    
    // 补全缺失的日期数据
    private List<DailyNursingCount> fillMissingDates(List<DailyNursingCount> original, int days) {
        List<DailyNursingCount> completeList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (int i = 0; i < days; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -i);
            String dateStr = sdf.format(cal.getTime());
            
            DailyNursingCount found = original.stream()
                .filter(item -> dateStr.equals(item.getDate()))
                .findFirst()
                .orElse(new DailyNursingCount(dateStr, 0));
            
            completeList.add(found);
        }
        
        // 按日期倒序排列
        completeList.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        return completeList;
    }
    
    
    public ResponseBean<List<PopularNursingProject>> getPopularProjects(Integer topN) {
        try {
            // 参数校验
            if (topN == null || topN <= 0) {
                return new ResponseBean<>(500, "参数必须大于0");
            }
            
            // 查询数据库
            List<PopularNursingProject> result = 
                nsd.findPopularProjectsLast7Days(topN);
            
            return new ResponseBean<>(200, "查询成功", result);
        } catch (Exception e) {
            return new ResponseBean<>(500, "查询失败");
        }
    }

}
