package com.huang.stock.config;


import com.huang.stock.utils.IdWorker;
import com.huang.stock.utils.ParserStockInfoUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CommonConfig {
    /*
    * 密码加密器  BCryptPasswordEncoder方法采用SHA-256对密码进行加密
    *
    * */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IdWorker idWorker(){
        //参数1：机器id 参数2：机房id 一般由运维人员确定
        return new IdWorker(1L,2L);
    }
    /*
     * 定义解析股票大盘、外盘、个股 板块相关信息的工具类
     * */
    @Bean
    public ParserStockInfoUtil parserStockInfoUtil(){
        return new ParserStockInfoUtil(idWorker());
    }
}
