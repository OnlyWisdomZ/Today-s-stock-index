package com.huang.stock;

import com.huang.SysUserApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes = SysUserApp.class)
public class TestRedis {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("myName","张三");
        String myName =redisTemplate.opsForValue().get("myName");
        System.out.println(myName);
    }
}
