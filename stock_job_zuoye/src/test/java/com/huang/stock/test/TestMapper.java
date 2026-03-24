package com.huang.stock.test;

import com.google.common.collect.Lists;
import com.huang.stock.mapper.StockBusinessMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class TestMapper {
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Test
    public void test01(){
        List<String> stockIds = stockBusinessMapper.getStockIds();
        System.out.println(stockIds);
        //大盘业务前缀
        stockIds = stockIds.stream().map(code -> code.startsWith("6") ? "sh" + code : "sz" + code).collect(Collectors.toList());
        System.out.println(stockIds);
        //将个股编码组成的大集合拆分成若干个小集合
        Lists.partition(stockIds,15).forEach(codes->{
            System.out.println("size"+codes.size()+":"+codes);
        });
    }
}
