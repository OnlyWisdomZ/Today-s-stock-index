package com.huang.stock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * 定义板块相关的值对象
 * */
@Schema(description = "定义板块相关的值对象 */")
@ConfigurationProperties(prefix = "stock")
@Data
@Component
public class StockBlockRtInfoConfig {
    //封装国外A股大盘编码集合
    @Schema(description = "封装国外A股大盘编码集合")
    private List<String> label;
}
