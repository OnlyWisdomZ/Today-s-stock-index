package com.huang.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/*
 * 个股周K数据封装
 * */
@Schema(description = "个股周K数据封装 */")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4WeekDomain {
    //一周内平均价
    @Schema(description = "一周内平均价")
    private BigDecimal avgPrice;
    //一周内最低价
    @Schema(description = "一周内最低价")
    private BigDecimal minPrice;
    //周一开盘价
    @Schema(description = "周一开盘价")
    private BigDecimal openPrice;
    //一周内最高价
    @Schema(description = "一周内最高价")
    private BigDecimal maxPrice;
    //周五收盘价（如果当前日期不到周五，则显示最新价格）
    @Schema(description = "周五收盘价（如果当前日期不到周五，则显示最新价格）")
    private BigDecimal closePrice;
    //日期
    @Schema(description = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date mxTime;
    //股票编码
    @Schema(description = "股票编码")
    @JsonProperty("code")
    private String code;
}
