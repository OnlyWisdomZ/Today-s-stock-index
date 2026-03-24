package com.huang.stock.service.impl;

import com.google.common.collect.Lists;
import com.huang.stock.mapper.*;
import com.huang.stock.pojo.domain.InnerBlockDomain;
import com.huang.stock.pojo.entity.StockMarketIndexInfo;
import com.huang.stock.pojo.entity.StockOuterMarketIndexInfo;
import com.huang.stock.pojo.entity.StockRtInfo;
import com.huang.stock.service.StockTimerTaskService;
import com.huang.stock.utils.DateTimeUtil;
import com.huang.stock.utils.IdWorker;
import com.huang.stock.utils.ParseType;
import com.huang.stock.utils.ParserStockInfoUtil;
import com.huang.stock.vo.StockBlockRtInfoConfig;
import com.huang.stock.vo.StockInfoConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {
    /*
     * 获取国内大盘的数据信息
     * */
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private StockBlockRtInfoConfig stockBlockRtInfoConfig;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;


    private HttpEntity httpEntity;
    @Override
    public void getInnerMarketInfo() {
        //1.采集原始数据
        String url =stockInfoConfig.getMarketUrl()+String.join(",",stockInfoConfig.getInner());
        HttpHeaders headers =new HttpHeaders();
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
        HttpEntity httpEntity =new HttpEntity(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,httpEntity, String.class);
        int statusCodeValue =responseEntity.getStatusCodeValue();
        if(statusCodeValue!=200){
            log.error("当前时间点:{},采集数据失败，http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),statusCodeValue);
            return;
        }
        //获取js数据
        String jsData =responseEntity.getBody();
        log.info("当前时间点:{},采集原始数据内容:{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),jsData);
        //2.js正则解析数据
        String reg ="var hq_str_(.+)=\"(.+)\";";
        Pattern pattern =Pattern.compile(reg);
        Matcher matcher =pattern.matcher(jsData);
        ArrayList<StockMarketIndexInfo> entities =new ArrayList<>();
        while(matcher.find()){
            //1.获取大盘的编码
            String marketCode = matcher.group(1);//相当于给它一个下标
            //获取其他信息
            String otherInfo = matcher.group(2);
            //将其他字符串以逗号切割 获取大盘的详细信息
            String[] splitArr = otherInfo.split(",");
            //大盘名称
            String marketName =splitArr[0];
            //获取当前大盘的开盘点
            BigDecimal openPoint = new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint = new BigDecimal(splitArr[2]);
            //大盘当前点
            BigDecimal curPoint = new BigDecimal(splitArr[3]);
            //大盘最高点
            BigDecimal maxPoint = new BigDecimal(splitArr[4]);
            //大盘最低点
            BigDecimal minPoint = new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt = Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol = new BigDecimal(splitArr[9]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30]+""+splitArr[31]).toDate();

            //3.解析数据封装entity
            StockMarketIndexInfo info =StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .curPoint(curPoint)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeVolume(tradeVol)
                    .tradeAmount(tradeAmt)
                    .curTime(curTime)
                    .build();//通过建造者模式赋值
            entities.add(info);
        }
        log.info("解析数据完毕");
        //4.调用mybatis批量入库
        int count =stockMarketIndexInfoMapper.insertBatch(entities);
        if(count>0){
            log.info("当前时间:{},插入数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),entities);
        }else{
            log.info("当前时间:{},插入数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),entities);
        }
    }

    @Override
    public void getStockRtIndex() {
        List<String> stockIds = stockBusinessMapper.getStockIds();
        //大盘业务前缀
        stockIds = stockIds.stream().map(code -> code.startsWith("6") ? "sh" + code : "sz" + code).collect(Collectors.toList());
        //一次性将所有的集合拼接到url地址中 会导致地址过长 参数过多
        //String url =stockInfoConfig.getMarketUrl()+String.join(",",stockIds);

        //将个股编码组成的大集合拆分成若干个小集合 40-->15  15  10
        long startTime =System.currentTimeMillis();
        Lists.partition(stockIds,15).forEach(codes->{
            /*//分批次采集
            String url =stockInfoConfig.getMarketUrl()+String.join(",",codes);
            HttpHeaders headers =new HttpHeaders();
            headers.add("Referer","https://finance.sina.com.cn/stock/");
            headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
            HttpEntity httpEntity =new HttpEntity(headers);
            ResponseEntity<String> responseEntity =restTemplate.exchange(url,HttpMethod.GET,httpEntity, String.class);
            int statusCodeValue =responseEntity.getStatusCodeValue();
            if(statusCodeValue!=200){
                log.error("当前时间点:{},采集数据失败，http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),statusCodeValue);
                return;
            }
            //获取js数据
            String jsData =responseEntity.getBody();
            List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
            log.info("采集个股数据:{}",list);
            //批量插入数据
            int count =stockRtInfoMapper.insertBatch(list);
            if(count>0){
                //大盘数据采集完成之后 通知backend刷新缓存
                //发送日期对象 接收日期和当前日期进行对比 能够判断出数据的延迟时长 用于运维的通知处理
                rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());
                log.info("当前时间:{},插入数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            }else{
                log.info("当前时间:{},插入数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            }*/
            /*new Thread(()->{
                //分批次采集
                String url =stockInfoConfig.getMarketUrl()+String.join(",",codes);
                HttpHeaders headers =new HttpHeaders();
                headers.add("Referer","https://finance.sina.com.cn/stock/");
                headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
                HttpEntity httpEntity =new HttpEntity(headers);
                ResponseEntity<String> responseEntity =restTemplate.exchange(url,HttpMethod.GET,httpEntity, String.class);
                int statusCodeValue =responseEntity.getStatusCodeValue();
                if(statusCodeValue!=200){
                    log.error("当前时间点:{},采集数据失败，http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),statusCodeValue);
                    return;
                }
                //获取js数据
                String jsData =responseEntity.getBody();
                List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
                log.info("采集个股数据:{}",list);
                //批量插入数据
                int count =stockRtInfoMapper.insertBatch(list);
                if(count>0){
                    //大盘数据采集完成之后 通知backend刷新缓存
                    //发送日期对象 接收日期和当前日期进行对比 能够判断出数据的延迟时长 用于运维的通知处理
                    rabbitTemplate.convertAndSend("stockExchange","inner.market",new Date());
                    log.info("当前时间:{},插入数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
                }else{
                    log.info("当前时间:{},插入数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
                }
            }).start();

        });
        long endTime=System.currentTimeMillis();
        long takeTime =endTime-startTime;
        log.info("本次采集花费时间:{}ms",takeTime);*/
        //方案2:引入线程池
        /*threadPoolTaskExecutor.execute(() -> {
            //1.1分批次采集
            String url =stockInfoConfig.getMarketUrl()+String.join(",",codes);
                //1.2 维护请求头 添加防盗链和用户标识
                HttpHeaders headers =new HttpHeaders();
                //防盗链
                headers.add("Referer","https://finance.sina.com.cn/stock/");
                //用户客户端标识
                headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
                //维护http请求实体对象
                HttpEntity httpEntity =new HttpEntity(headers);
            //发送请求
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
            //状态码
            int statusCodeValue =responseEntity.getStatusCodeValue();
            if(statusCodeValue!=200){
                log.error("当前时间点:{},采集数据失败，http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),statusCodeValue);
                //其他操作:发送邮件 企业微信 钉钉 给相关运维人员提醒
                return;
            }
            //获取js格式数据
            String jsData =responseEntity.getBody();
            List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
            log.info("采集个股数据:{}",list);
            //批量插入
            int count =stockRtInfoMapper.insertBatch(list);
            if(count>0){
                //大盘采集数据完毕之后 通知backend刷新缓存
                //发送日期对象 接收日期与当前日期的对比 能判断出数据的延迟时长 用于运维的通知处理
                rabbitTemplate.convertAndSend("stockExchange","inner.market", new Date());
                log.info("当前时间:{},插入大盘数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            }else{
                log.info("当前时间:{},插入大盘数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
            }
            long endTime=System.currentTimeMillis();
            long takeTime =endTime-startTime;
            log.info("本次采集花费时间:{}ms",takeTime);
            });*/
        });
    }
    /*
     * 定时采集板块数据
     * */
    @Override
    public void getBlockInfos() {
        //1.获取股票最新交易时间点(精确到分钟； 秒 毫秒置为0 因为这样做等值查询效率会更高)
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //因为没有最新数据 所以curDate mCodes 等后续完成股票采集就可以了 再将其删除

        //Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //curDate=DateTime.parse("2022-12-21 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date curDate =curDateTime.toDate();
        //2.获取板块编码
        List<String> bCodes = stockBlockRtInfoConfig.getLabel();
        //3.调用Mapper查询数据
        List<InnerBlockDomain> blockRtData= stockBlockRtInfoMapper.getBlockRtInfo(curDate,bCodes);

    }
    /*
     * 定时采集外盘数据
     * */
    @Override
    public void getOuterMarketInfos() {
        //1.采集原始数据
        String url =stockInfoConfig.getMarketUrl()+String.join(",",stockInfoConfig.getOuter());
        HttpHeaders headers =new HttpHeaders();
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
        HttpEntity httpEntity =new HttpEntity(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET,httpEntity, String.class);
        int statusCodeValue =responseEntity.getStatusCodeValue();
        if(statusCodeValue!=200){
            log.error("当前时间点:{},采集数据失败，http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),statusCodeValue);
            return;
        }
        //获取js数据
        String jsData =responseEntity.getBody();
        log.info("当前时间点:{},采集原始数据内容:{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),jsData);
        //2.js正则解析数据
        String reg ="var hq_str_(.+)=\"(.+)\";";
        Pattern pattern =Pattern.compile(reg);
        Matcher matcher =pattern.matcher(jsData);
        ArrayList<StockOuterMarketIndexInfo> entities =new ArrayList<>();
        while(matcher.find()){
            //1.获取大盘的编码
            String marketCode = matcher.group(1);//相当于给它一个下标
            //获取其他信息
            String otherInfo = matcher.group(2);
            //将其他字符串以逗号切割 获取大盘的详细信息
            String[] splitArr = otherInfo.split(",");
            //大盘名称
            String marketName =splitArr[0];
            //获取当前大盘的当前点
            BigDecimal curPoint = new BigDecimal(splitArr[1]);
            //涨跌值
            BigDecimal updown = new BigDecimal(splitArr[2]);
            //涨幅
            BigDecimal rose = new BigDecimal(splitArr[3]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[4]+""+splitArr[5]).toDate();
            //3.解析数据封装entity
            StockOuterMarketIndexInfo info =StockOuterMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketCode(marketCode)
                    .marketName(marketName)
                    .curPoint(curPoint)
                    .updown(updown)
                    .rose(rose)
                    .curTime(curTime)
                    .build();//通过建造者模式赋值
            entities.add(info);
        }
        log.info("解析数据完毕");
        //4.调用mybatis批量入库
        int count =stockOuterMarketIndexInfoMapper.insertBatch(entities);
        if(count>0){
            log.info("当前时间:{},插入数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),entities);
        }else{
            log.info("当前时间:{},插入数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),entities);
        }
    }

    @PostConstruct
    public void initData(){
        HttpHeaders headers =new HttpHeaders();
        headers.add("Referer","https://finance.sina.com.cn/stock/");
        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");
        httpEntity =new HttpEntity(headers);
    }
}
