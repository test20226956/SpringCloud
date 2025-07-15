package com.neu.SP01.dao;


import com.neu.SP01.po.CustOutRecordDTO;
import com.neu.SP01.po.OutRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 对应t_out_record
 */
@Mapper
public interface OutRecordDao {
    /*
    护工模块下的客户管理外出记录
     */
    //添加用户的外出记录
    @Insert("insert into yyzx_st.t_out_record values (null,#{customerId},#{applyTime},#{examineTime},0,#{adminId},#{reason},#{outTime},#{expectedReturnTime},#{nurseId},null)")
    void addOutRecord(OutRecord or);
    //根据老人查找对应的外出申请(外出申请功能页面中点击老人的申请记录)（多表查询 t_out_record t_customer）
    List<CustOutRecordDTO> findOutRecordByCustomerId(@Param("customerId") Integer customerId);
    //给用户添加回院时间
    @Update("update yyzx_st.t_out_record set actual_return_time=#{actualReturnTime} where out_record_id=#{outRecordId}")
    void AddActualReturnTime(@Param("outRecordId")Integer outRecordId, @Param("actualReturnTime")String actualReturnTime);
    @Select("select * from yyzx_st.t_out_record where out_record_id=#{outRecordId}")
    OutRecord findOutRecordById(@Param("outRecordId")Integer outRecordId);

    //根据申请时间查询老人的外出申请记录
    List<CustOutRecordDTO> findOutRecordByTime(@Param("customerId")Integer customerId,@Param("outTime")String outTime);

}
