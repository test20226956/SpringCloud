package com.neu.SP01.dao;


import org.apache.ibatis.annotations.*;

@Mapper
public interface CheckInRecordDao {

    /*获取客户当前有效的入住记录ID*/
    @Select("SELECT check_in_record_id FROM t_check_in_record " +
            "WHERE customer_id = #{customerId} AND state = 1") // state=1表示有效入住
    Integer findActiveCheckInId(@Param("customerId") Integer customerId);
    

    /*获取入住记录关联的床位记录ID*/
    @Select("SELECT bed_record_id FROM t_bed_record " +
            "WHERE check_in_record_id = #{checkInRecordId} AND state = 1")
    Integer findActiveBedRecordId(@Param("checkInRecordId") Integer checkInRecordId);

}
