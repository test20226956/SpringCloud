package com.neu.SP01.dao;

import com.neu.SP01.po.Meal;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 对应t_meal
 */
@Mapper
public interface MealDao {

    
    List<Meal> findAllMeals();
    //查找食物
    @Select("<script>" +
            "SELECT * FROM t_meal " +
            "WHERE state=1 " +
            "<if test='name != null and name != \"\"'>" +
            "   AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "<if test='type != null'>" +
            "   AND type = #{type} " +
            "</if>" +
            " LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Meal> findMealsByPage(@Param("name") String name, 
                             @Param("type") Integer type,
                             @Param("offset") long offset,
                             @Param("pageSize") long pageSize);
    @Select("<script>" +
            "SELECT COUNT(*) FROM t_meal " +
            "WHERE state=1 " +
            "<if test='name != null and name != \"\"'>" +
            "   AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "<if test='type != null'>" +
            "   AND type = #{type} " +
            "</if>" +
            "</script>")
    long countMeals(@Param("name") String name, @Param("type") Integer type);
    
    //添加食物
    @Insert("insert into t_meal (name,type,state) values (#{name},#{type},1)")
    Integer addMeal(Meal meal);
    
    //修改食物信息
    @Update("update t_meal set name=#{name},type=#{type} where meal_id=#{mealId}")
    Integer updateMeal(Meal meal);
    //删除存在的食物
    @Update("update t_meal set state=0 where meal_id =#{mealId}")
    Integer deleteMeal(Integer mealId);
}
