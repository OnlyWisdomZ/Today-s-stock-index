package com.huang.stock.pojo.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockCodeSearchDomain {
    //股票编码
    @Schema(description = "股票编码")
    private String code;

    //股票名称
    @Schema(description = "股票名称")
    private String name;
}
