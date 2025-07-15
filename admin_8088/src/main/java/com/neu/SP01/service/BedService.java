package com.neu.SP01.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.neu.SP01.dao.BedDao;
import com.neu.SP01.dao.RoomDao;
import com.neu.SP01.po.Bed;
import com.neu.SP01.po.BedSta;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.po.Room;

@Service
@Transactional(rollbackFor = Exception.class) // 添加事务注解，任何异常都会回滚
public class BedService {
	@Autowired
    private BedDao bd;
	@Autowired
    private RoomDao rd;
	
	private static final Logger log = LoggerFactory.getLogger(CheckOutRecordService.class);
	//查空床列表
	public ResponseBean<List<Bed>> searchFreeBed(Integer roomId){
		try {
            // 参数校验
            if (roomId == null) {
             
                return new ResponseBean<>(500, "房间ID不能为空", null);
            }
            // 查询数据库
            List<Bed> freeBeds = bd.searchFreeBed(roomId);
            
            // 处理查询结果
            if (freeBeds == null || freeBeds.isEmpty()) {
                
                return new ResponseBean<>(500, "该房间没有空闲床位",null);
            }
            
            return new ResponseBean<>(200, "查询成功", freeBeds);
            
        } catch (Exception e) {
            
            return new ResponseBean<>(500, "系统错误，查询失败", null);
        }
	}
    
	
	 // 全院床位统计
	public ResponseBean<BedSta> getAllBedStats() {
	    try {
	        BedSta stats = bd.countAllBedStatus();
	        if (stats == null || stats.getTotal() == null) {
	            
	            return new ResponseBean<>(500, "床位统计数据异常");
	        }
	        return new ResponseBean<>(200, "统计成功", stats);
	    } catch (DataAccessException e) {
	        
	        return new ResponseBean<>(500, "数据库错误: " + e.getCause().getMessage(), null);
	    } catch (Exception e) {
	        
	        return new ResponseBean<>(500, "系统错误", null);
	    }
	}
	
	// 按楼层查询房间列表及楼层统计
    public ResponseBean<Map<String, Object>> getFloorDetails(Integer floor) {
        try {
            if (floor == null) {
                floor = 1; // 默认第一层
            }

            // 1. 获取楼层统计
            BedSta floorStats = bd.countBedStatusByFloor(floor);
            if (floorStats == null) {
                return new ResponseBean<>(500, "获取楼层统计失败", null);
            }

            // 2. 获取房间列表
            List<Room> rooms = rd.findRoomsByFloor(floor);
            if (rooms == null) {
                return new ResponseBean<>(500, "获取房间列表失败", null);
            }
            if (floorStats.getTotal() == 0 && rooms.isEmpty()) {
                return new ResponseBean<>(500, "该楼层暂无数据",null);
            }

            // 3. 封装结果
            Map<String, Object> result = new HashMap<>();
            result.put("floorStats", floorStats);
            result.put("rooms", rooms);

            return new ResponseBean<>(200, "楼层详情查询成功", result);
        } catch (Exception e) {
            log.error("查询楼层详情异常", e);
            return new ResponseBean<>(500, "系统异常: " + e.getMessage(), null);
        }
    }
    //查找房间中床位详情信息
    public ResponseBean<List<Map<String, Object>>> getBedsByRoomAndFloor(
    	    String roomNumber, 
    	    Integer floor) {
    	    
    	    try {
    	        // 参数校验（简单写法）
    	        if (roomNumber == null || roomNumber.trim().isEmpty()) {
    	            return new ResponseBean<>(500, "房间号不能为空", null);
    	        }
    	        if (floor == null) {
    	            return new ResponseBean<>(500, "楼层不能为空", null);
    	        }

    	        // 剩余逻辑保持不变...
    	        List<Map<String, Object>> beds = bd.findBedsByRoomAndFloor(roomNumber, floor);
    	        
    	        if (beds == null) {
    	            return new ResponseBean<>(500, "查询床位信息失败", null);
    	        }
    	        if (beds.isEmpty()) {
    	            return new ResponseBean<>(500, "该房间暂无床位数据", Collections.emptyList());
    	        }

    	        return new ResponseBean<>(200, "查询成功", beds);
    	        
    	    } catch (Exception e) {
    	        log.error("查询床位详情异常，房间号: {}, 楼层: {}", roomNumber, floor, e);
    	        return new ResponseBean<>(500, "系统异常: " + e.getMessage(), null);
    	    }
    	}
    
}