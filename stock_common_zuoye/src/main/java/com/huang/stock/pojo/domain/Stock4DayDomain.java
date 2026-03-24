package com.huang.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/*
 * 个股日K数据封装
 * */
@Schema(description = "个股日K数据封装 */")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4DayDomain {
    //日期
    @Schema(description = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date date;
    //交易量
    @Schema(description = "交易量")
    private Long tradeAmt;
    //股票编码
    @Schema(description = "股票编码")
    private String code;
    //最低价格
    @Schema(description = "最低价格")
    private BigDecimal lowPrice;
    //股票名称
    @Schema(description = "股票名称")
    private String name;
    //最高价
    @Schema(description = "最高价")
    private BigDecimal highPrice;
    //开盘价
    @Schema(description = "开盘价")
    private BigDecimal openPrice;
    //交易金额
    @Schema(description = "交易金额")
    private BigDecimal tradeVol;
    //当前收盘价
    @Schema(description = "当前收盘价")
    private BigDecimal closePrice;
    //当前价格(最新价格)
    @Schema(description = "前收盘价")
    private BigDecimal preClosePrice;
}
