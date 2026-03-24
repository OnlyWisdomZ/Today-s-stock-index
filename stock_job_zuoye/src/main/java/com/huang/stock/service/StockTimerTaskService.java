package com.huang.stock.service;

public interface StockTimerTaskService {
    /*
    * 获取国内大盘数据信息
    * */
    void getInnerMarketInfo();

    /*
     * 定义获取分钟级股票数据
     * */
    void getStockRtIndex();
    /*
     * 定时采集板块数据
     * */
    void getBlockInfos();
    /*
     * 定时采集外盘数据
     * */
    void getOuterMarketInfos();
}
