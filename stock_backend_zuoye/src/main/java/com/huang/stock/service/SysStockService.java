package com.huang.stock.service;

import com.huang.stock.pojo.domain.*;
import com.huang.stock.pojo.entity.StockRtInfo;
import com.huang.stock.vo.resp.PageResult;
import com.huang.stock.vo.resp.R;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SysStockService {
    /*
     * 获取国内大盘最新数据
     * */
    R<List<InnerMarketDomain>> getInnerMarketInfo();
    /*
     * 获取沪深板块最新数据
     * */
    R<List<InnerBlockDomain>> getInnerBlockInfo();
    /*
    * 分页查询股票最新数据 并且按照涨幅排序查询
    * */
    R<PageResult<StockUpdownDomain>> getStockUpdownInfo(Integer page, Integer pageSize);

    R<List<StockUpdownDomain>> getStockIncreaseInfo();
    /*
     * 获取国外大盘最新数据
     * */
    R<List<OuterMarketDomain>> getOuterMarketInfo();
    /*
     * 统计每最新股票交易日每分钟的涨跌停的股票数量
     * */
    R<Map<String, List>> getStockUpdownCount();

    /*
     * 将指定页的股票数据导出到excel表中
     * */
    void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response);
    /*
     * 统计国内大盘T日和T-1日成交量对比功能
     * */
    R<Map<String, List>> getComparedStockTradeAmt();

    /*
     * 统计最新交易时间点下股票 在各个涨幅区间的数量
     * */
    R<Map> getIncreaseRangeInfo();

    /*
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * 总结:获取指定股票T日分时数据
     * */
    R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(String stockCode);

    /*
    * 查询指定股票每天产生的数据，组装成日K线数据
    * */
    R<List<Stock4DayDomain>> getStockScreenDaySharing(String stockCode);
    /*
     * 根据股票代码查询股票信息
     * */
    R<List<StockCodeSearchDomain>> searchStockByCode(String searchStr);
    /*
     * 根据股票代码 查询个股描述
     * */
    R<StockDescribeDomain> getStockDescribe(String code);

    /*
     * 查询指定股票每周产生的数据，组装成周K线数据
     * */
    R<List<Stock4WeekDomain>> getStockScreenWeekLine(String StockCode);

    /*
     * 个股最新分时行情
     * */
    R<Stock4SecondDetailDomain> getStockScreenSecondDetail(String stockCode);

    /*
     * 查询个股实时交易流水
     * */
    R<List<Stock4SecondStatementDomain>> getStockScreenSecondStatement(String stockCode);




    /*
    * 只查询开始时间 结束时间
    * *//*
    List<StockRtInfo> getStockScreenDayKLine(String stockCode, Date startDate, Date endDate);
    *//*
    * 查询指定时间范围下股票每日的最大日期
    * */

}
