package com.neu.SP01.dao;

import com.neu.SP01.po.CheckOutRecord;
import com.neu.SP01.po.CustCheckOutDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CheckOutRecordDao {
	/*====护工模块 用户管理退住申请====*/
	//添加用户的退住申请
	@Insert("insert into yyzx_st.t_check_out_record values (null,#{customerId},0,#{type},#{applyTime},#{examineTime},#{adminId},#{nurseId},#{reason})")
	void InsertCheckOutRecord(CheckOutRecord cor);
	//查看用户的退住申请记录
	List<CustCheckOutDTO> findCheckOutRecordByCustomerId(@Param("customerId") Integer customerId);

}
