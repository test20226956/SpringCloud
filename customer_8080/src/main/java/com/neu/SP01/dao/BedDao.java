package com.neu.SP01.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;

import com.neu.SP01.po.Bed;
import com.neu.SP01.po.BedSta;

@Mapper
public interface BedDao {
	
	// 更新床位状态
    @Update("UPDATE t_bed SET available = 1 WHERE bed_id = #{bedId}")
    int updateBedStatus(@Param("bedId") Integer bedId);
    /*更新床位状态*/
    @Update("UPDATE t_bed SET available = #{available} WHERE bed_id = #{bedId}")
    int updateBedStatus1(
            @Param("bedId") Integer bedId,
            @Param("available") Integer available);
    
 // 更新床位状态为空闲
    @Update("UPDATE t_bed SET available = 0 WHERE bed_id = #{bedId}")
    int setBedAvailable(@Param("bedId") Integer bedId);
    
    /*更新实际床位状态为可用*/
    @Update("UPDATE t_bed SET available = 0 WHERE bed_id = " +
            "(SELECT bed_id FROM t_bed_record WHERE bed_record_id = #{bedRecordId})")
    int updatePhysicalBedStatus(@Param("bedRecordId") Integer bedRecordId);
    
    /*更新实际床位状态为外出*/
    @Update("UPDATE t_bed SET available = 2 WHERE bed_id = " +
            "(SELECT bed_id FROM t_bed_record WHERE bed_record_id = #{bedRecordId})")
    int updatePhysicalBedStatus2(@Param("bedRecordId") Integer bedRecordId);
    /*更新实际床位状态为有人*/
    @Update("UPDATE t_bed SET available = 1 WHERE bed_id = " +
            "(SELECT bed_id FROM t_bed_record WHERE bed_record_id = #{bedRecordId})")
    int updatePhysicalBedStatus3(@Param("bedRecordId") Integer bedRecordId);

 // 根据房间ID和床号查询床位信息
    @Select("SELECT * FROM t_bed WHERE room_id = #{roomId} AND bed_number = #{bedNumber}")
    @Results({
        @Result(property = "bedId", column = "bed_id"),
        @Result(property = "roomId", column = "room_id"),
        @Result(property = "bedNumber", column = "bed_number"),
        @Result(property = "available", column = "available")
    })
    Bed findBedByRoomAndNumber(@Param("roomId") Integer roomId, 
                             @Param("bedNumber") Integer bedNumber);
    
    /*统计全院床位状态*/
    @Select("SELECT " +
            "COUNT(*) AS total, " +
            "SUM(CASE WHEN available = 0 THEN 1 ELSE 0 END) AS free, " +
            "SUM(CASE WHEN available = 1 THEN 1 ELSE 0 END) AS taken, " +
            "SUM(CASE WHEN available = 2 THEN 1 ELSE 0 END) AS goOut " +
            "FROM t_bed")
    BedSta countAllBedStatus() throws DataAccessException;;

    /*按楼层统计床位状态*/
    @Select("SELECT " +
            "COUNT(*) AS total, " +
            "SUM(CASE WHEN b.available = 0 THEN 1 ELSE 0 END) AS free, " +
            "SUM(CASE WHEN b.available = 1 THEN 1 ELSE 0 END) AS taken, " +
            "SUM(CASE WHEN b.available = 2 THEN 1 ELSE 0 END) AS goOut " +
            "FROM t_bed b " +
            "JOIN t_room r ON b.room_id = r.room_id " +
            "WHERE r.floor = #{floor}")
    BedSta countBedStatusByFloor(@Param("floor") Integer floor);
    
    /*通过房间号和楼层查询床位详情（包含入住人信息）*/
    @Select("SELECT b.*, c.name AS customer_name " +
    	       "FROM t_bed b " +
    	       "JOIN t_room r ON b.room_id = r.room_id " +
    	       "LEFT JOIN t_bed_record br ON b.bed_id = br.bed_id AND br.state = 1 " +
    	       "LEFT JOIN t_check_in_record cir ON br.check_in_record_id = cir.check_in_record_id AND cir.state = 1 " +  // 修正列名
    	       "LEFT JOIN t_customer c ON cir.customer_id = c.customer_id " +
    	       "WHERE r.room_number = #{roomNumber} AND r.floor = #{floor}")
    List<Map<String, Object>> findBedsByRoomAndFloor(
    	    @Param("roomNumber") String roomNumber, 
    	    @Param("floor") Integer floor);
    
    @Select("SELECT b.bed_id, b.bed_number, b.room_id, b.available " +
            "FROM t_bed b " +
            "WHERE b.room_id = #{roomId} AND b.available = 0 " +
            "ORDER BY b.bed_number ASC")
    List<Bed> searchFreeBed(@Param("roomId") Integer roomId);
    
    
    

    /*获取床位状态*/
    @Select("SELECT available FROM t_bed WHERE bed_id = #{bedId}")
    Integer getBedStatus(@Param("bedId") Integer bedId);

    
    
    /*根据位置信息查找床ID*/
    @Select("SELECT b.bed_id FROM t_bed b " +
            "JOIN t_room r ON b.room_id = r.room_id " +
            "WHERE r.floor = #{floor} AND r.room_number = #{roomNumber} AND b.bed_number = #{bedNumber}")
    Integer findBedIdByLocation(
            @Param("floor") Integer floor,
            @Param("roomNumber") String roomNumber,
            @Param("bedNumber") Integer bedNumber);

   
}