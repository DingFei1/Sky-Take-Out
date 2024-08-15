package com.sky.controller.user;

import com.sky.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
public class ShopController {
    private final static String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate<Object, Integer> redisTemplate;

    @GetMapping("/status")
    public Result<Integer> getStatus() {
        return Result.success(redisTemplate.opsForValue().get(KEY));
    }
}
