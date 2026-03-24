package com.huang.stock.mq;

import com.github.benmanes.caffeine.cache.Cache;
import com.huang.stock.service.SysStockService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class StockMqMsgListener {
    @Autowired
    private Cache<String,Object> coffeinecache;
    @Autowired
    private SysStockService sysstockService;
    @RabbitListener(queues = "innerMarketQueue")
    public void refreshInnerMarketInfo(Date startTime){
        //统计当前时间点与发送消息时间点的差值 如果超过一分钟则警告
        //获取时间毫秒值
        Long difftime = DateTime.now().getMillis() - new DateTime(startTime).getMillis();
        if(difftime>60000L){
            log.error("大盘发送消息时间,{},超时,{},毫秒",new DateTime(startTime).getMillis());
        }
        //刷新缓存
        //剔除原来旧的数据
        coffeinecache.invalidate("innerMarketKey");
        //调用方法 刷新数据
        sysstockService.getInnerMarketInfo();
    }
}
