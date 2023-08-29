package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    @Autowired
    RedisTemplate redisTemplate;

    public static final String key = "SHOP_STATUS";
    @PutMapping("{status}")
    @ApiOperation("修改店铺营业状态")
    public Result setStatus(@PathVariable Integer status){
        log.info("修改店铺营业状态为：{}",status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(key, status);
        return Result.success();
    }
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getShopStatus(){
//        Integer val = (Integer) redisTemplate.opsForValue().get(key);
//        log.info("查询到店铺营业状态为:{}",val);
//        return Result.success(val);
        return Result.success((Integer) redisTemplate.opsForValue().get(key));
    }
}
