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
* 个股交易流水行情数据封装
* */
@Schema(description = "个股交易流水行情数据封装 */")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4SecondStatementDomain {
    //当前时间
    @Schema(description = "当前时间")
    @JsonFormat(pattern = "yyyyMMddHHmm",timezone = "Asia/Shanghai")
    private Date date;
    //交易量
    @Schema(description = "交易量")
    private Long tradeAmt;
    //交易金额
    @Schema(description = "交易金额")
    private BigDecimal tradeVol;
    //交易价格
    @Schema(description = "交易价格")
    private BigDecimal tradePrice;
}
