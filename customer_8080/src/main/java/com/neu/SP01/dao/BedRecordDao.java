package com.neu.SP01.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neu.SP01.po.BedRecord;

@Mapper
public interface BedRecordDao {
	// 添加床位记录
    @Insert("INSERT INTO t_bed_record(bed_id, check_in_record_id, state, start_time,end_time) " +
            "VALUES(#{bedRecord.bedId}, #{bedRecord.checkInRecordId}, " +
            "#{bedRecord.state}, #{bedRecord.startTime}, #{bedRecord.endTime})")
    int insertBedRecord(@Param("bedRecord") BedRecord bedRecord);
    
    // 获取客户当前显示的床位记录
    @Select("SELECT * FROM t_bed_record WHERE check_in_record_id = " +
            "(SELECT check_in_record_id FROM t_check_in_record " +
            "WHERE customer_id = #{customerId}) " +
            "AND state = 1 LIMIT 1")
    BedRecord findActiveBedRecordByCustomer(@Param("customerId") Integer customerId);
    
    // 逻辑删除床位记录（设置状态为隐藏）
    @Update("UPDATE t_bed_record SET state = 0 WHERE bed_record_id = #{bedRecordId}")
    int hideBedRecord(@Param("bedRecordId") Integer bedRecordId);
    
 // 获取当前有效的床位记录
    @Select("SELECT * FROM t_bed_record WHERE check_in_record_id=#{checkInRecordId} AND state=1")
    BedRecord findActiveBedRecord(@Param("checkInRecordId") Integer checkInRecordId);
    
    // 更新床位记录的结束时间
    @Update("UPDATE t_bed_record SET end_time=#{endTime} WHERE bed_record_id=#{bedRecordId}")
    int updateBedEndTime(@Param("bedRecordId") Integer bedRecordId, 
                       @Param("endTime") String endTime);
    
    
    /*多条件组合查询床位使用详情*/
    @Select("<script>" +
            "SELECT " +
            "   br.bed_record_id, " +
            "   c.name AS customer_name, " +
            "   c.customer_id ," +
            "   c.gender, " +
            "   c.identity, " +
            "   r.building_number, " +
            "   r.floor, " +
            "   r.room_number, " +
            "   b.bed_number, " +
            "   br.start_time, " +
            "   br.end_time, " +
            "   br.state " +
            "FROM t_bed_record br " +
            "JOIN t_bed b ON br.bed_id = b.bed_id " +
            "JOIN t_room r ON b.room_id = r.room_id " +
            "JOIN t_check_in_record cir ON br.check_in_record_id = cir.check_in_record_id " +
            "JOIN t_customer c ON cir.customer_id = c.customer_id " +
            "<where>" +
            "   <if test='name != null and name != \"\"'>" +
            "       AND c.name LIKE CONCAT('%', #{name}, '%') " +
            "   </if>" +
            "   <if test='startTime != null and startTime != \"\"'>" +
            "       AND br.start_time >= #{startTime} " +
            "   </if>" +
            "   <if test='status != null'>" +
            "       AND br.state = #{status} " +
            "   </if>" +
            "</where>" +
            "ORDER BY br.start_time DESC " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Map<String, Object>> findBedUsageDetails(
            @Param("name") String name,
            @Param("startTime") String startTime,
            @Param("status") Integer status,
            @Param("offset") long offset,
            @Param("pageSize") long pageSize);

    /*统计符合条件的记录总数*/
    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM t_bed_record br " +
            "JOIN t_bed b ON br.bed_id = b.bed_id " +
            "JOIN t_room r ON b.room_id = r.room_id " +
            "JOIN t_check_in_record cir ON br.check_in_record_id = cir.check_in_record_id " +
            "JOIN t_customer c ON cir.customer_id = c.customer_id " +
            "<where>" +
            "   <if test='name != null and name != \"\"'>" +
            "       AND c.name LIKE CONCAT('%', #{name}, '%') " +
            "   </if>" +
            "   <if test='startTime != null and startTime != \"\"'>" +
            "       AND br.start_time >= #{startTime} " +
            "   </if>" +
            "   <if test='status != null'>" +
            "       AND br.state = #{status} " +
            "   </if>" +
            "</where>" +
            "</script>")
    long countBedUsageDetails(
            @Param("name") String name,
            @Param("startTime") String startTime,
            @Param("status") Integer status);
    
    /*更新床位记录的结束时间*/
    @Update("UPDATE t_bed_record SET end_time = #{endTime} WHERE bed_record_id = #{bedRecordId}")
    int updateEndTime(@Param("bedRecordId") Integer bedRecordId, 
                     @Param("endTime") String endTime);
    
    
    @Select("SELECT * FROM t_bed_record WHERE bed_record_id = #{bedRecordId}")
    BedRecord findById(Integer bedRecordId);
    

    /*根据客户ID查找当前有效的床位记录*/
    @Select("SELECT br.* FROM t_bed_record br " +
            "JOIN t_check_in_record cir ON br.check_in_record_id = cir.check_in_record_id " +
            "WHERE cir.customer_id = #{customerId} AND br.state = 1")
    BedRecord findActiveByCustomerId(@Param("customerId") Integer customerId);

    /*更新床位记录*/
    @Update("UPDATE t_bed_record SET " +
            "bed_id = #{bedId}, " +
            "check_in_record_id = #{checkInRecordId}, " +
            "state = #{state}, " +
            "start_time = #{startTime}, " +
            "end_time = #{endTime} " +
            "WHERE bed_record_id = #{bedRecordId}")
    int updateBedRecord(BedRecord bedRecord);

    /*新增床位记录*/
    @Insert("INSERT INTO t_bed_record(" +
            "bed_id, check_in_record_id, state, start_time, end_time) " +
            "VALUES(" +
            "#{bedId}, #{checkInRecordId}, #{state}, #{startTime}, #{endTime})")
    @Options(useGeneratedKeys = true, keyProperty = "bedRecordId")
    int insertBedRecord1(BedRecord bedRecord);

    

    
    
}