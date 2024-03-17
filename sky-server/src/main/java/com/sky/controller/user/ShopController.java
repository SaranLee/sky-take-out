package com.sky.controller.user;

import com.sky.constant.ShopConstant;
import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("UserShopController")
@RequestMapping("/user/shop")
@Api(tags = "用户相关店铺操作")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation("用户获取店铺状态")
    public Result<Integer> getStatus() {
        ValueOperations vOps = redisTemplate.opsForValue();
        Integer status = (Integer) vOps.get(ShopConstant.STATUS_KEY);
        return Result.success(status);
    }
}
