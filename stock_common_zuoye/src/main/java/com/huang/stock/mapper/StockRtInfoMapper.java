package com.huang.stock.mapper;

import com.huang.stock.pojo.domain.*;
import com.huang.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author OnlyWisdomZ
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2025-07-19 11:44:12
* @Entity com.huang.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {




    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);
    /*
     * 统计每最新股票交易日每分钟的涨跌停的股票数量
     * @param startDate 开始时间
     * @param endDate  结束时间
     * @param flag 约定 1-->涨停  0-->跌停
     * */
    List<Map> getStockUpdownCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("flag") int flag);

    /*
     * 统计最新交易时间点下股票 在各个涨幅区间的数量
     * */
    List<Map> getIncreaseRangeInfo(@Param("curDate") Date curDate);

    /*
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * 总结:获取指定股票T日分时数据
     * */
    List<Stock4MinuteDomain> getStockScreenTimeSharing(@Param("openDate") Date openDate, @Param("endDate") Date endDate, @Param("stockCode") String stockCode);

    /*
     * 查询指定股票每天产生的数据，组装成日K线数据
     * */
    List<Stock4DayDomain> getStockScreenDaySharing(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("stockCode") String stockCode);
    /*
     * 定义获取分钟级股票数据
     * */
    int insertBatch(@Param("list") List<StockRtInfo> list);

    List<Date> getMxTime4EvryDay(@Param("stockCode") String stockCode, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<Stock4DayDomain> getStock4Dkline2(@Param("stockCode") String stockCode, @Param("mxTimes") List<Date> mxTimes);

    /*List<Date> getStockMaxTimeEvrDay(@Param("stockCode") String stockCode,
                                     @Param("startDate") Date startDate,
                                     @Param("endDate") Date endDate);*/

    /*List<StockRtInfo> selectByMaxTimes(@Param("stockCode") String stockCode,
                                          @Param("maxTimeList") List<Date> maxTimeList);*/
    /*
     * 根据股票代码查询股票信息
     * */
    List<StockCodeSearchDomain> searchStockByCode(@Param("searchStr") String searchStr);

    /*
     * 查询指定股票每周产生的数据，组装成周K线数据
     * */
    List<Stock4WeekDomain> getStockScreenWKLine(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("stockCode") String stockCode);
    /*
     * 个股最新分时行情
     * */
    Stock4SecondDetailDomain getStockScreenSecondDetail(@Param("openDate") Date openDate, @Param("endDate") Date endDate, @Param("stockCode") String stockCode);

    /*
     * 查询个股实时交易流水
     * */
    List<Stock4SecondStatementDomain> getStockScreenSecondStatement(@Param("stockCode") String stockCode);
}
