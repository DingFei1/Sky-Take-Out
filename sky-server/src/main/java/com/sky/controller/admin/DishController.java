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

/**
 * Dish Controller for administrators
 */
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    DishService dishService;

    /**
     * Create a new dish
     * @param dishDTO Dish data transfer object
     * @return operation result with success message
     */
    @PostMapping
    public Result<Void> save(@RequestBody DishDTO dishDTO) {
        dishService.save(dishDTO);
        return Result.success();
    }

    /**
     * Query dish information by pagination
     * @param dishPageQueryDTO Dish page data transfer object
     * @return operation result with the information of dishes and success message
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Delete a list of dishes based on the ids
     * @param ids The id of the dishes to be deleted
     * @return operation result with success message
     */
    @DeleteMapping
    public Result<Void> delete(@RequestParam List<Long> ids) {
        dishService.delete(ids);
        return Result.success();
    }

    /**
     * Query the dish based on the id
     * @param id The id of the dish to be queried
     * @return operation result with success message
     */
    @GetMapping("/{id}")
    public Result<DishVO> searchById(@PathVariable Long id) {
        DishVO dishVO = dishService.searchById(id);
        return Result.success(dishVO);
    }

    /**
     * Update the dish
     * @param dishDTO The dish data transfer object
     * @return operation result with success message
     */
    @PutMapping
    public Result<Void> update(@RequestBody DishDTO dishDTO) {
        dishService.update(dishDTO);
        return Result.success();
    }

    /**
     * Query the dish based on the category id
     * @param categoryId The id of the category to be queried
     * @return operation result with the list of dish value objects and success message
     */
    @GetMapping("/list")
    public Result<List<DishVO>> searchByCategoryId(@RequestParam String categoryId) {
        List<DishVO> dishVOList = dishService.searchByCategoryId(categoryId);
        return Result.success(dishVOList);
    }

    /**
     * Modify the status of the dish based on the id
     * @param status The status modified
     * @param id The id of the dish whose status will be modified
     * @return operation result with success message
     */
    @PostMapping("/status/{status}")
    public Result<Void> EnableOrDisable(@PathVariable Integer status, @RequestParam(value = "id") Long id) {
        dishService.enableOrDisable(status, id);
        return Result.success();
    }
}