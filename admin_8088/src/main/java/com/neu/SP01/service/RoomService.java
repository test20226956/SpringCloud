package com.neu.SP01.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neu.SP01.dao.RoomDao;
import com.neu.SP01.po.ResponseBean;
import com.neu.SP01.po.Room;

@Service
public class RoomService {
	@Autowired
    private RoomDao rd;
	public ResponseBean<List<Room>> searchRoom(Integer floor){
            // 参数校验
            if (floor == null) {
                return new ResponseBean<>(500, "楼层参数不能为空", null);
            }

            // 查询数据库
            List<Room> rooms = rd.findRooms(floor);
            
            // 处理查询结果
            if (rooms == null || rooms.isEmpty()) {
                return new ResponseBean<>(500, "该楼层没有房间",null);
            }
            
            return new ResponseBean<>(200, "查询成功", rooms);
            
	}
    
}