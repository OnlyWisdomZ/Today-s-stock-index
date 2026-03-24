package com.huang.stock.pojo.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
 * 股票描述信息
 * */
@Schema(description = "股票描述信息 */")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDescribeDomain {
    //股票编码
    @Schema(description = "股票编码")
    private String code;
    //行业，也就是行业板块名称
    @Schema(description = "行业，也就是行业板块名称")
    private String trade;
    //公司主营业务
    @Schema(description = "公司主营业务")
    private String business;
    //公司名称
    @Schema(description = "公司名称")
    private String name;
}
