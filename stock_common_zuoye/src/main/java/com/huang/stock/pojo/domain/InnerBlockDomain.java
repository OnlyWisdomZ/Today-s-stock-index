package com.huang.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
/*
* 股票板块Domain
* */
@Schema(description = "定义国内板块的领域对象 */")
@Data
public class InnerBlockDomain {
    /*
     * 公司数量
     * */
    @Schema(description = "公司数量 */")
    private Integer companyNum;
    /*
     * 交易量
     * */
    @Schema(description = "交易量 */")
    private Long tradeAmt;
    /*
     * 板块编码
     * */
    @Schema(description = "板块编码 */")
    private String code;
    /*
     * 平均价格
     * */
    @Schema(description = "平均价格 */")
    private BigDecimal avgPrice;
    /*
     * 板块名称
     * */
    @Schema(description = "板块名称 */")
    private String name;
    /*
     * 当前时间
     * */
    @Schema(description = "当前时间 */")
    @JsonFormat(pattern= "yy-MM-dd HH:mm")
    private Date  curDate;
    /*
     * 交易总金额
     * */
    @Schema(description = "交易总金额 */")
    private Long tradeVol;
    /*
     * 涨幅
     * */
    @Schema(description = "涨幅 */")
    private BigDecimal updownRate;

}
