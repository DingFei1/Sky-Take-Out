package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SetMealService {
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void addSetMeal(SetmealDTO setmealDTO);

    SetmealVO searchById(Long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

    void updateSetMeal(SetmealDTO setmealDTO);

    void changeStatus(Integer status, Long id);

    void deleteBySetMealIds(List<Long> ids);
}
