package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Category management for Administrators
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "Interface relevant with Category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    /**
     * Add new category
     * @param categoryDTO Category data transfer object
     * @return Operation result with success message
     */
    @PostMapping
    @ApiOperation("Add new category")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO){
        log.info("Add new category：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * Query employee information by pagination
     * @param categoryPageQueryDTO Category page query transfer object
     * @return Operation result with categories' information and success message
     */
    @GetMapping("/page")
    @ApiOperation("Query employee information by pagination")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("Query employee information by pagination：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Delete one category
     * @param id Category id to be deleted
     * @return Operation result with success message
     */
    @DeleteMapping
    @ApiOperation("Delete category")
    public Result<String> deleteById(Long id){
        log.info("Delete one category：{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * Update category
     * @param categoryDTO Category data transfer object
     * @return Operation result with success message
     */
    @PutMapping
    @ApiOperation("Update category")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * Enable or Disable one category
     * @param status Status to be done
     * @param id Designated category id
     * @return Operation result with success message
     */
    @PostMapping("/status/{status}")
    @ApiOperation("Enable or disable category")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * Query category based on the given type
     * @param type Required type
     * @return Operation result with categories' information and success message
     */
    @GetMapping("/list")
    @ApiOperation("Query category based on the given type")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}