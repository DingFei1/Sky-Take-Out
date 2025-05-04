package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void save(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void delete(List<Long> ids);

    DishVO searchById(Long id);

    void update(DishDTO dishDTO);

    List<DishVO> searchByCategoryId(String categoryId);

    /**
     * 条件查询菜品和口味
     * @param categoryId
     * @return
     */
    List<DishVO> listWithFlavor(Long categoryId);

    void enableOrDisable(Integer status, Long id);
}
