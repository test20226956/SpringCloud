package com.neu.SP01.dao;

import com.neu.SP01.po.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 对应表t_nursing_service
 */
@Mapper
public interface NursingServiceDao {
    /*======对应原型护工 日常护理 显示用户的护理服务=====*/
    List<NursingServiceDailyDTO> findNursingServiceByCustomerId(@Param("customerId")Integer customerId);

    //按项目名字搜索用户的持有的护理服务
    List<NursingServiceDailyDTO> findNursingServiceByName(@Param("customerId")Integer customerId,@Param("name")String name);
    //对应原型护工 日常护理 添加用户护理记录，相应的对应护理服务的数量也应该减少
    @Update("UPDATE yyzx_st.t_nursing_service SET amount = amount - #{count} WHERE nursing_service_id = #{nursingServiceId}")
    void updateNursingServiceAmount(@Param("count")Integer count,@Param("nursingServiceId")Integer nursingServiceId);
}
