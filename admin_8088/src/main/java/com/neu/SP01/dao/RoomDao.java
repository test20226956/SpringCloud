package com.neu.SP01.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.neu.SP01.po.Room;


@Mapper
public interface RoomDao {
    
	// 根据楼层号和房间号查询房间信息
    @Select("SELECT * FROM t_room WHERE floor = #{floor} AND room_number = #{roomNumber}")
    @Results({
        @Result(property = "roomId", column = "room_id"),
        @Result(property = "roomNumber", column = "room_number"),
        @Result(property = "floor", column = "floor"),
        @Result(property = "buildingNumber", column = "building_number"),
        @Result(property = "bedCount", column = "bed_count")
    })
    Room findRoomByFloorAndNumber(@Param("floor") Integer floor, 
                                @Param("roomNumber") String roomNumber);
    
    /*按楼层查询房间列表*/
    @Select("SELECT * FROM t_room WHERE floor = #{floor} ORDER BY room_number")
    List<Room> findRoomsByFloor(@Param("floor") Integer floor);
    
    @Select("SELECT * FROM t_room WHERE floor = #{floor}")
    List<Room> findRooms(@Param("floor") Integer floor);
}

