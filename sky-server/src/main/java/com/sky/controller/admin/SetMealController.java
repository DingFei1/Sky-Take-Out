package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    SetMealService setMealService;

    @GetMapping("/page")
    public Result<PageResult> querySetMeal(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setMealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    @CacheEvict(cacheNames = "set_meal_cache", key = "setmealDTO.categoryId")
    public Result<Void> addSetMeal(@RequestBody SetmealDTO setmealDTO) {
        setMealService.addSetMeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> searchById(@PathVariable Long id) {
        SetmealVO setmealVO = setMealService.searchById(id);
        return Result.success(setmealVO);
    }
}
