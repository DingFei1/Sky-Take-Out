package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    List<Long> getSetMealByDishIds(List<Long> dishIds);

    @Insert("INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies) VALUES (#{setmealId}, #{dishId}, #{name}, #{price}, #{copies})")
    void insert(SetmealDish setMealDish);

    @Select("SELECT * FROM setmeal_dish WHERE setmeal_id = #{setMealId}")
    List<SetmealDish> searchBySetMealId(Long setMealId);
}
