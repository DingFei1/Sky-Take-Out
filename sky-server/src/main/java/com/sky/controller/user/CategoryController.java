package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Category management for Customers
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "Customer Side - Interface for Category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    /**
     * Query category based on the given type
     * @param type Required type
     * @return Operation result with categories' information and success message
     */
    @GetMapping("/list")
    @ApiOperation("Query category based on the given type")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}