package com.neu.SP01.dao;

import org.apache.ibatis.annotations.*;


@Mapper
public interface BedDao {
    /*更新实际床位状态为有人*/
    @Update("UPDATE t_bed SET available = 1 WHERE bed_id = " +
            "(SELECT bed_id FROM t_bed_record WHERE bed_record_id = #{bedRecordId})")
    int updatePhysicalBedStatus3(@Param("bedRecordId") Integer bedRecordId);
}