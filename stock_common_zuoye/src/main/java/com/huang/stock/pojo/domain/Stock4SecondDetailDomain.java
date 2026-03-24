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
* 个股最新分时数据封装
* */
@Schema(description = "个股最新分时数据封装 */")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4SecondDetailDomain {
    //最新交易量
    @Schema(description = "最新交易量")
    private Long tradeAmt;
    //前收盘价格
    @Schema(description = "前收盘价格")
    private BigDecimal preClosePrice;
    //最低价
    @Schema(description = "最低价")
    private BigDecimal lowPrice;
    //最高价
    @Schema(description = "最高价")
    private BigDecimal highPrice;
    //开盘价
    @Schema(description = "开盘价")
    private BigDecimal openPrice;
    //交易金额
    @Schema(description = "交易金额")
    private BigDecimal tradeVol;
    //当前价格(最新价格)
    @Schema(description = "当前价格(最新价格)")
    private BigDecimal tradePrice;
    //当前日期
    @Schema(description = "当前日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date curDate;
}
