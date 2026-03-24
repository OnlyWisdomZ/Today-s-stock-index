package com.huang.stock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/*
* 定义股票相关的值对象
* */
@Schema(description = "定义股票相关的值对象 */")
@ConfigurationProperties(prefix = "stock")
@Data
@Component
public class StockInfoConfig {
    //封装国内A股大盘编码集合
    @Schema(description = "封装国内A股大盘编码集合")
    private List<String> inner;
    //封装外盘编码集合
    @Schema(description = "封装外盘编码集合")
    private List<String> outer;
    //股票涨幅区间标题集合
    @Schema(description = "股票涨幅区间标题集合")
    private List<String> upDownRange;
    /**
     * 大盘 外盘 个股的公共URL
     */
    @Schema(description = "大盘 外盘 个股的公共URL")
    private String marketUrl;
    /**
     * 板块采集URL
     */
    @Schema(description = "板块采集URL")
    private String blockUrl;


}
