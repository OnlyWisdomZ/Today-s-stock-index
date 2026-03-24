package com.huang.stock.job;

import com.huang.stock.service.StockTimerTaskService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class StockJob {
    @Autowired
    private StockTimerTaskService stockTimerTaskService;
    @XxlJob("demoJobHandler")
    public void demoJobHandler() throws Exception {
        XxlJobHelper.log("XXL-JOB, Hello World.");
        System.out.println("当前时间点为:{}"+ DateTime.now());
        for (int i = 0; i < 5; i++) {
            XxlJobHelper.log("beat at:" + i);
            TimeUnit.SECONDS.sleep(2);
        }
        // default success
    }
    /*
    * 定时采集任务 采集国内大盘数据
    * */
    @XxlJob("getInnerMarketInfos")
    public void getStockInnerMarketInfos(){
        stockTimerTaskService.getInnerMarketInfo();
    }
    /*
    * 定时采集A股数据
    * */
    @XxlJob("getStockInfos")
    public void getStockInfos(){
        stockTimerTaskService.getStockRtIndex();
    }
    /*
    * 定时采集板块数据
    * */
    @XxlJob("getBlockInfos")
    public void getBlockInfos(){
        stockTimerTaskService.getBlockInfos();
    }
    /*
    * 定时采集外盘数据
    * */
    @XxlJob("getOuterMarketInfos")
    public void getOuterMarketInfos(){
        stockTimerTaskService.getOuterMarketInfos();
    }
}
