package com.neu.SP01.dao;

import com.neu.SP01.po.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CustomerDao {
	/*=============护工模块 显示该护工下的老人 对应请求路径"/user/showUserCust"=========*/
	List<CustDailyNursingDTO> findUserCust(@Param("userId") Integer userId);
	//客户管理模块显示老人信息（其实是增加了身份证号，入住时间以及到期时间这三项内容）
	List<CustNursingManageDTO> findUserCustManage(@Param("userId")Integer userId);
	//根据老人姓名模糊搜索
	List<CustDailyNursingDTO> findUserCustByName(@Param("userId")Integer userId,@Param("name")String name);
	//客户管理模块根据老人姓名模糊搜索（其实是增加了身份证号，入住时间以及到期时间这三项内容）
	List<CustNursingManageDTO> findUserCustManageByName(@Param("userId")Integer userId,@Param("name")String name);

}
