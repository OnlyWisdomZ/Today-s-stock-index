package com.huang.stock.controller;

import com.github.pagehelper.PageInfo;
import com.huang.stock.pojo.domain.*;
import com.huang.stock.pojo.entity.StockRtInfo;
import com.huang.stock.service.SysStockService;
import com.huang.stock.vo.resp.PageResult;
import com.huang.stock.vo.resp.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/*
* 定义股票相关接口控制器
* */
@Tag(name = "定义股票相关接口控制器 */", description = "定义股票相关接口控制器 */")
@RestController
@RequestMapping("/api/quot")
public class SysStockController {
    @Autowired
    private SysStockService sysStockService;
    /*
    * 获取国内大盘最新数据
    * */
    @Operation(summary = "获取国内大盘最新数据 */", description = "获取国内大盘最新数据 */")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo(){
    return sysStockService.getInnerMarketInfo();
    }
    /*
     * 获取沪深板块最新数据
     * */
    @Operation(summary = "获取沪深板块最新数据 */", description = "获取沪深板块最新数据 */")
    @GetMapping("/sector/all")
    public R<List<InnerBlockDomain>> getInnerBlockInfo(){
        return sysStockService.getInnerBlockInfo();
    }
    /*
    * 分页查询股票最新数据 并且按照涨幅排序查询
    * */
    @Parameters({
            @Parameter(name = "page", description = "", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "", in = ParameterIn.QUERY)
    })
    @Operation(summary = "分页查询股票最新数据 并且按照涨幅排序查询 */", description = "分页查询股票最新数据 并且按照涨幅排序查询 */")
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getStockUpdownInfo(@RequestParam(name="page",required = false,defaultValue = "1")Integer page,
                                                               @RequestParam(name="pageSize",required = false,defaultValue = "20")Integer pageSize){
        return sysStockService.getStockUpdownInfo(page,pageSize);
    }
    /*
     * 分页查询股票最新数据 并且按照涨幅降序排序查询 查询前四条
     * */
    @Operation(summary = "分页查询股票最新数据 并且按照涨幅降序排序查询 查询前四条 */", description = "分页查询股票最新数据 并且按照涨幅降序排序查询 查询前四条 */")
    @GetMapping("/stock/increase")
    public R<List<StockUpdownDomain>> getStockIncreaseInfo(){
        return sysStockService.getStockIncreaseInfo();
    }

    /*
     * 统计每最新股票交易日每分钟的涨跌停的股票数量
     * */
    @Operation(summary = "统计每最新股票交易日每分钟的涨跌停的股票数量 */", description = "统计每最新股票交易日每分钟的涨跌停的股票数量 */")
    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getStockUpdown(){
        return sysStockService.getStockUpdownCount();
    }

    /*
     * 获取国外大盘最新数据
     * */
    @Operation(summary = "获取国外大盘最新数据 */", description = "获取国外大盘最新数据 */")
    @GetMapping("/external/index")
    public R<List<OuterMarketDomain>> getOuterMarketInfo(){
        return sysStockService.getOuterMarketInfo();
    }

    /*
     * 将指定页的股票数据导出到excel表中
     * */
    @Parameters({
            @Parameter(name = "page", description = "", in = ParameterIn.QUERY),
            @Parameter(name = "pageSize", description = "", in = ParameterIn.QUERY)
    })
    @Operation(summary = "将指定页的股票数据导出到excel表中 */", description = "将指定页的股票数据导出到excel表中 */")
    @GetMapping("/stock/export")
    public void exportStockUpDownInfo(@RequestParam(name="page",required = false,defaultValue = "1")Integer page,
                                      @RequestParam(name="pageSize",required = false,defaultValue = "20")Integer pageSize,
                                      HttpServletResponse response) throws IOException {
        sysStockService.exportStockUpDownInfo(page,pageSize,response);
    }
    /*
     * 统计国内大盘T日和T-1日成交量对比功能
     * */
    @Operation(summary = "统计国内大盘T日和T-1日成交量对比功能 */", description = "统计国内大盘T日和T-1日成交量对比功能 */")
    @GetMapping("/stock/tradeAmt")
    public  R<Map<String,List>> getComparedStockTradeAmt(){
        return sysStockService.getComparedStockTradeAmt();
    }

    /*
     * 统计最新交易时间点下股票 在各个涨幅区间的数量
     * */
    @Operation(summary = "统计最新交易时间点下股票 在各个涨幅区间的数量 */", description = "统计最新交易时间点下股票 在各个涨幅区间的数量 */")
    @GetMapping("/stock/updown")
    public R<Map> getIncreaseRangeInfo(){
        return sysStockService.getIncreaseRangeInfo();
    }

    /*
    * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
    * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
    * 总结:获取指定股票T日分时数据
    * */
    @Parameter(name = "code", description = "", in = ParameterIn.QUERY, required = true)
    @Operation(summary = "功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据； 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点 总结:获取指定股票T日分时数据 */", description = "功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据； 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点 总结:获取指定股票T日分时数据 */")
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(@RequestParam(value="code",required = true) String stockCode){
        return sysStockService.getStockScreenTimeSharing(stockCode);
    }
    /*
    * 查询指定股票每天产生的数据，组装成日K线数据
    * */
    @Parameter(name = "code", description = "", in = ParameterIn.QUERY, required = true)
    @Operation(summary = "查询指定股票每天产生的数据，组装成日K线数据 */", description = "查询指定股票每天产生的数据，组装成日K线数据 */")
    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4DayDomain>> getStockScreenDaySharing(@RequestParam(value="code",required = true) String stockCode){
        return sysStockService.getStockScreenDaySharing(stockCode);
    }
    /*
     * 先查询指定时间范围下股票每日的最大日期，再查询指定时间点下股票数据
     * *//*
    @GetMapping("/stock/screen/dkline")
    public R<List<StockRtInfo>> getStockScreenDayKLine(@RequestParam(required = true) String stockCode,
                                                       @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                       @RequestParam(required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        return sysStockService.getStockScreenDayKLine(stockCode, startDate, endDate);

    }*/

    /*
     * 根据股票代码查询股票信息
     * */
    @Parameter(name = "searchStr", description = "", in = ParameterIn.QUERY, required = true)
    @Operation(summary = "根据股票代码查询股票信息 */", description = "根据股票代码查询股票信息 */")
    @GetMapping("/stock/search")
    public R<List<StockCodeSearchDomain>> searchStockByCode(@RequestParam(value = "searchStr",required = true) String searchStr){
        return  sysStockService.searchStockByCode(searchStr);
    }

    /*
    * 根据股票代码 查询个股描述
    * */
    @Parameter(name = "code", description = "", in = ParameterIn.QUERY, required = true)
    @Operation(summary = "根据股票代码 查询个股描述 */", description = "根据股票代码 查询个股描述 */")
    @GetMapping("/stock/describe")
    public R<StockDescribeDomain> getStockDescribe(@RequestParam(value = "code",required = true) String code){
        return sysStockService.getStockDescribe(code);
    }

    /*
     * 查询指定股票每周产生的数据，组装成周K线数据
     * */
    @Parameter(name = "code", description = "", in = ParameterIn.QUERY, required = true)
    @Operation(summary = "查询指定股票每周产生的数据，组装成周K线数据 */", description = "查询指定股票每周产生的数据，组装成周K线数据 */")
    @GetMapping("/stock/screen/weekkline")
    public R<List<Stock4WeekDomain>> getStockScreenWeekLine(@RequestParam(value="code",required = true) String stockCode){
        return sysStockService.getStockScreenWeekLine(stockCode);
    }

    /*
    * 个股最新分时行情
    * */
    @GetMapping("/stock/screen/second/detail")
    public R<Stock4SecondDetailDomain> getStockScreenSecondDetail(@RequestParam(value="code",required = true) String stockCode){
        return sysStockService.getStockScreenSecondDetail(stockCode);
    }

    /*
    * 查询个股实时交易流水
    * */
    @GetMapping("/stock/screen/second")
    public R<List<Stock4SecondStatementDomain>> getStockScreenSecondStatement(@RequestParam(value="code",required = true) String stockCode){
        return  sysStockService.getStockScreenSecondStatement(stockCode);
    }

}
