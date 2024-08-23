package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    DishService dishService;

    @PostMapping
    public Result<Void> save(@RequestBody DishDTO dishDTO) {
        dishService.save(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    public Result<Void> delete(@RequestParam List<Long> ids) {
        dishService.delete(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<DishVO> searchById(@PathVariable Long id) {
        DishVO dishVO = dishService.searchById(id);
        return Result.success(dishVO);
    }

    @PutMapping
    public Result<Void> update(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<DishVO>> searchByCategoryId(@RequestParam String categoryId) {
        List<DishVO> dishVOList = dishService.searchByCategoryId(categoryId);
        return Result.success(dishVOList);
    }
}
