package com.neu.SP01.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.neu.SP01.po.Family;
@Mapper
public interface FamilyDao {
	@Insert("INSERT INTO t_family(customer_id, name, relation, tel,state) " +
            "VALUES(#{family.customerId}, #{family.name}, #{family.relation}, #{family.tel},1)")
    int insertFamily(@Param("family") Family family);
	 //根据ID家人
	   @Update("update t_family set state=0 where customer_id=#{customerId} AND state=1")
	   Integer deleteFamily(Integer customerId);
}
