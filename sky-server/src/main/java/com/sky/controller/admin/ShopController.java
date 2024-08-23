package com.sky.controller.admin;

import com.sky.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
public class ShopController {
    private final static String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate<Object, Integer> redisTemplate;

    @PutMapping("/{status}")
    public Result<Void> setStatus(@PathVariable Integer status) {
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    @GetMapping("/status")
    public Result<Integer> getStatus() {
        return Result.success(redisTemplate.opsForValue().get(KEY));
    }
}
