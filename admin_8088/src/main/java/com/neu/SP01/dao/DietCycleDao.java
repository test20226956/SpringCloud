package com.neu.SP01.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.neu.SP01.po.DietCycle;
import com.neu.SP01.po.DietCycleDetail;

@Mapper
public interface DietCycleDao {

    
    /*添加膳食周期安排*/
    @Insert("insert into t_diet_cycle (date,meal_id,type,state) values (#{date},#{mealId},#{type},1)")
    Integer addDietCycle(DietCycle dietCycle);
    /*删除膳食周期安排*/
    @Delete("DELETE FROM t_diet_cycle WHERE diet_cycle_id = #{dietCycleId}")
    Integer deleteDietCycle(@Param("dietCycleId") Integer dietCycleId);
    /*更新膳食周期安排*/
    @Update("UPDATE t_diet_cycle SET date = #{date}, meal_id = #{mealId}, " +
            "type = #{type} ,state=#{state} WHERE diet_cycle_id = #{dietCycleId}")
    Integer updateDietCycle(DietCycle dietCycle);
    
    /*根据日期查询膳食安排*/
    @Select("SELECT dc.*, m.name AS mealName, m.img AS mealImg, m.type AS mealType,m.state AS mealState " +
            "FROM t_diet_cycle dc " +
            "LEFT JOIN t_meal m ON dc.meal_id = m.meal_id " +
            "WHERE dc.date = #{date} ")  
    List<DietCycleDetail> selectDietCycleWithMealByDate(@Param("date") String date);
    
    /*根据日期和type查询膳食安排*/
    @Select("SELECT dc.*, m.name AS mealName, m.img AS mealImg, m.type AS mealType,m.state AS mealState " +
            "FROM t_diet_cycle dc " +
            "LEFT JOIN t_meal m ON dc.meal_id = m.meal_id AND m.state=1 " +
            "WHERE dc.date = #{date} AND dc.type=#{type} ")  
    List<DietCycleDetail> selectDietCycleWithMealByType(@Param("date") String date,@Param("type") Integer type);
    
    /*根据ID查询膳食周期安排*/
    @Select("SELECT * FROM diet_cycle WHERE meal_id = #{mealId}")
    DietCycle findDietCycleByMealId(@Param("mealId") Integer mealId);
    
    @Update("UPDATE t_diet_cycle SET state=0 WHERE meal_id = #{mealId}")
    Integer updateDietCycleByMealId(@Param("mealId") Integer mealId);
    
    
    
    

    
    
   


    
}