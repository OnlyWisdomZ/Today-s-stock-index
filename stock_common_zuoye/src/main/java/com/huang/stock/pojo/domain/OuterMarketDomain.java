package com.huang.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "定义国外大盘的领域对象 */")
@Data
public class OuterMarketDomain {
    /*
     * 大盘编码
     * */
    @Schema(description = "大盘编码 */")
    private String code;
    /*
     * 指数名称
     * */
    @Schema(description = "大盘名称 */")
    private String name;
    /*
     * 当前点
     * */
    @Schema(description = "当前点 */")
    private BigDecimal curPoint;
    /*
     * 涨跌值
     * */
    @Schema(description = "涨跌值 */")
    private BigDecimal upDown;
    /*
     * 涨幅
     * */
    @Schema(description = "涨幅 */")
    private BigDecimal rose;
    /*
     * 当前时间
     * */
    @Schema(description = "当前时间 */")
    @JsonFormat(pattern= "yy-MM-dd HH:mm")
    private Date curTime;
}
