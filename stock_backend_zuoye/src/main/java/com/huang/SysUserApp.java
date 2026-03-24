package com.huang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.huang.stock.mapper")
public class SysUserApp {
    public static void main(String[] args) {
        SpringApplication.run(SysUserApp.class);
    }
}
