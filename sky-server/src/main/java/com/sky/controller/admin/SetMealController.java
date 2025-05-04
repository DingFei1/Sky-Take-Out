package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
public class SetMealController {
    @Autowired
    SetMealService setMealService;

    @GetMapping("/page")
    public Result<PageResult> querySetMeal(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setMealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    //@CacheEvict(cacheNames = "set_meal_cache", key = "setmealDTO.categoryId")
    public Result<Void> addSetMeal(@RequestBody SetmealDTO setmealDTO) {
        setMealService.addSetMeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> searchById(@PathVariable Long id) {
        SetmealVO setmealVO = setMealService.searchById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    public Result<Void> updateSetMeal(@RequestBody SetmealDTO setmealDTO) {
        setMealService.updateSetMeal(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result<Void> changeStatus(@PathVariable Integer status, @RequestParam(value = "id") Long id) {
        setMealService.changeStatus(status, id);
        return Result.success();
    }

    @DeleteMapping
    public Result<Void> deleteBySetMealIds(@RequestParam List<Long> ids) {
        setMealService.deleteBySetMealIds(ids);
        return Result.success();
    }
}
