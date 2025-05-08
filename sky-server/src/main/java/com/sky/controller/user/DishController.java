package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Dish Controller for customers
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "Customer Side - Dish Browsing Interface")
public class DishController {
    @Autowired
    DishService dishService;

    @Autowired
    RedisTemplate<String, List<DishVO>> redisTemplate;

    /**
     * Query the dish based on the category id
     *
     * @param categoryId The id of the category to be queried
     * @return operation result with the list of dish value objects and success message
     */
    @GetMapping("/list")
    @ApiOperation("Query the dish based on the category id")
    public Result<List<DishVO>> list(Long categoryId) {
        // Check whether the cache contain the information of dishes
        String key = "dish_" + categoryId;
        List<DishVO> dishVOListInCache = redisTemplate.opsForValue().get(key);
        // If the cache contains, return result directly
        if(dishVOListInCache != null && !dishVOListInCache.isEmpty()) {
            return Result.success(dishVOListInCache);
        }

        // If the cache does not contain, query the information in the disk
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE); // Query the dish being sold

        // Query the dishes and their corresponding flavours
        List<DishVO> dishVOList = dishService.listWithFlavor(categoryId);

        redisTemplate.opsForValue().set(key, dishVOList); // Store it in cache synchronously

        return Result.success(dishVOList);
    }
}