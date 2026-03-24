package com.huang.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huang.stock.mapper.*;
import com.huang.stock.pojo.domain.*;
import com.huang.stock.pojo.entity.StockRtInfo;
import com.huang.stock.service.SysStockService;
import com.huang.stock.utils.DateTimeUtil;
import com.huang.stock.vo.StockBlockRtInfoConfig;
import com.huang.stock.vo.StockInfoConfig;
import com.huang.stock.vo.resp.PageResult;
import com.huang.stock.vo.resp.R;
import com.huang.stock.vo.resp.ResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysStockServiceImpl implements SysStockService {

    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockBlockRtInfoConfig stockBlockRtInfoConfig;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    // 汉字Unicode编码范围：\u4e00（最低）到\u9fa5（最高）
    private static final char CHINESE_MIN = '\u4e00';
    private static final char CHINESE_MAX = '\u9fa5';

    /*
     * 获取国内大盘最新数据
     * */
    @Override
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {
        //1.获取股票最新交易时间点(精确到分钟； 秒 毫秒置为0 因为这样做等值查询效率会更高)
        //DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //因为没有最新数据 所以curDate mCodes 等后续完成股票采集就可以了 再将其删除
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2022-12-28 14:59:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //Date curDate =curDateTime.toDate();
        //2.获取大盘编码
        List<String> mCodes = stockInfoConfig.getInner();
        //3.调用Mapper查询数据
        List<InnerMarketDomain> data = stockMarketIndexInfoMapper.getMarketInfo(curDate, mCodes);
        //4.封装数据并响应
        return R.ok(data);
    }

    @Override
    public R<List<InnerBlockDomain>> getInnerBlockInfo() {
        //1.获取股票最新交易时间点(精确到分钟； 秒 毫秒置为0 因为这样做等值查询效率会更高)
        //DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //因为没有最新数据 所以curDate mCodes 等后续完成股票采集就可以了 再将其删除

        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2022-12-21 09:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //Date curDate =curDateTime.toDate();
        //2.获取板块编码
        List<String> bCodes = stockBlockRtInfoConfig.getLabel();
        //3.调用Mapper查询数据
        List<InnerBlockDomain> blockRtData = stockBlockRtInfoMapper.getBlockRtInfo(curDate, bCodes);
        //4.封装数据并响应
        return R.ok(blockRtData);
    }

    @Override
    public R<PageResult<StockUpdownDomain>> getStockUpdownInfo(Integer page, Integer pageSize) {
        //1.获取股票最新交易时间点(精确到分钟； 秒 毫秒置为0 因为这样做等值查询效率会更高)
        //DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //因为没有最新数据 所以curDate 等后续完成股票采集就可以了 再将其删除
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2022-12-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        PageHelper.startPage(page, pageSize);//设置分页参数
        List<StockUpdownDomain> list = stockBlockRtInfoMapper.getStockUpdownInfoByTime(curDate);
        PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(list);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
        return R.ok(pageResult);
    }

    @Override
    public R<List<StockUpdownDomain>> getStockIncreaseInfo() {
        //1.获取股票最新交易时间点(精确到分钟； 秒 毫秒置为0 因为这样做等值查询效率会更高)
        //DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //因为没有最新数据 所以curDate 等后续完成股票采集就可以了 再将其删除
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2022-12-30 09:32:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<StockUpdownDomain> list = stockBlockRtInfoMapper.getStockIncreaseInfoByTime(curDate);
        return R.ok(list);
    }

    @Override
    public R<List<OuterMarketDomain>> getOuterMarketInfo() {
        Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        curDate = DateTime.parse("2023-05-18 15:58:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //Date curDate =curDateTime.toDate();
        //2.获取大盘编码
        List<String> oCodes = stockInfoConfig.getOuter();
        //3.调用Mapper查询数据
        List<OuterMarketDomain> data = stockOuterMarketIndexInfoMapper.getOuterMarketInfo(curDate, oCodes);
        //4.封装数据并响应
        return R.ok(data);
    }

    @Override
    public R<Map<String, List>> getStockUpdownCount() {
        //1.获取股票最新交易时间点(精确到分钟； 秒 毫秒置为0 因为这样做等值查询效率会更高)
        //DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //因为没有最新数据 所以curDate 等后续完成股票采集就可以了 再将其删除
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        curDateTime = DateTime.parse("2023-01-06 14:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = curDateTime.toDate();
        //2.获取最新交易时间点 对应的开盘时间点
        Date startDate = DateTimeUtil.getOpenDate(curDateTime).toDate();
        //3.统计涨停数据  约定mapper中flag入参：1-->涨停数据  0-->跌停数据
        List<Map> upList = stockRtInfoMapper.getStockUpdownCount(startDate, endDate, 1);
        //4.统计跌停数据
        List<Map> downList = stockRtInfoMapper.getStockUpdownCount(startDate, endDate, 0);
        //5.组装数据
        HashMap<String, List> info = new HashMap<>();
        info.put("upList", upList);
        info.put("downList", downList);
        return R.ok(info);
    }

    /*
     * 将指定页的股票数据导出到excel表中
     * */
    @Override
    public void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response) {
        //1.获取分页数据
        R<PageResult<StockUpdownDomain>> r = this.getStockUpdownInfo(page, pageSize);
        List<StockUpdownDomain> list = r.getData().getRows();
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        try {
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class).sheet("模板").doWrite(list);
        } catch (IOException e) {
            log.error("当前页码:{},每页大小:{},当前时间:{},异常信息:{}", page, pageSize,
                    DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), e.getMessage());
            //通知前端异常 稍后重试
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            R<Object> error = R.error(ResponseCode.ERROR);
            try {
                String jsonData = new ObjectMapper().writeValueAsString(error);
                response.getWriter().write(jsonData);
            } catch (IOException ex) {
                log.error("exportStockUpDownInfo:响应错误信息失败，时间:{}", page, pageSize,
                        DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }
        }
    }

    /*
     * 统计国内大盘T日和T-1日成交量对比功能
     * */
    @Override
    public R<Map<String, List>> getComparedStockTradeAmt() {
        //1.获取T日和T-1日的开始时间和结束时间
        //1.1 获取最新股票交易的日期范围 也就是T日的交易范围
        DateTime tEndDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        tEndDateTime = DateTime.parse("2022-12-28 14:59:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date tEndDate = tEndDateTime.toDate();
        Date tStartDate = DateTimeUtil.getPreDateTime(tEndDateTime).toDate();
        //获取T-1日的时间范围
        DateTime previousTradingDay = DateTimeUtil.getPreviousTradingDay(tEndDateTime);
        previousTradingDay = DateTime.parse("2022-12-27 14:59:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date tPreEndDate = previousTradingDay.toDate();
        //开盘时间
        Date tPreStartDate = DateTimeUtil.getOpenDate(previousTradingDay).toDate();
        //调用mapper查询
        List<Map> tData = stockMarketIndexInfoMapper.getSumAmtInfo(tStartDate, tEndDate, stockInfoConfig.getInner());
        List<Map> tPreData = stockMarketIndexInfoMapper.getSumAmtInfo(tPreStartDate, tPreEndDate, stockInfoConfig.getInner());
        HashMap data = new HashMap();
        data.put("amtList", tData);
        data.put("yesAmtList", tPreData);
        //4.响应数据
        return R.ok(data);
    }

    /*
     * 统计最新交易时间点下股票 在各个涨幅区间的数量
     * */
    @Override
    public R<Map> getIncreaseRangeInfo() {
        //1.获取股票最新交易时间点
        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());//T日的截止时间点
        curDateTime = DateTime.parse("2023-07-07 14:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = curDateTime.toDate();
        //调用mapper查询
        List<Map> infos = stockRtInfoMapper.getIncreaseRangeInfo(curDate);
        //获取有序的涨幅区间标题集合
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        //方式一 将顺序的涨幅区间内的每个对象转化成map对象即可
        /*List<Map> allInfos =new ArrayList<>();
        Map tmp =null;
        for(String title: upDownRange) {
            for (Map info : infos) {
                tmp =info;
                break;
            }
            if(tmp==null){
                tmp =new HashMap();
                tmp.put("count",0);
                tmp.put("title",title);
            }
            allInfos.add(tmp);
        }*/

        //方式二 stream流
        List<Map> allInfos = upDownRange.stream().map(title -> {
            Optional<Map> result = infos.stream().filter(map ->
                    map.containsValue(title)).findFirst();
            //判断是否符合过滤的元素
            if (result.isPresent()) {
                return result.get();
            } else {
                HashMap<String, Object> tmp = new HashMap<>();
                tmp.put("count", 0);
                tmp.put("title", title);
                return tmp;
            }
        }).collect(Collectors.toList());
        //组装数据
        HashMap<String, Object> data = new HashMap<>();
        data.put("time", curDateTime.toString("yyyy-MM-dd HH:mm:ss"));
        data.put("infos", allInfos);
        //返回数据
        return R.ok(data);
    }

    /*
     * 功能描述：查询单个个股的分时行情数据，也就是统计指定股票T日每分钟的交易数据；
     * 如果当前日期不在有效时间内，则以最近的一个股票交易时间作为查询时间点
     * 总结:获取指定股票T日分时数据
     * */
    @Override
    public R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(String stockCode) {
        //1.获取股票最新交易时间点
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());//T日的截止时间点
        endDateTime = DateTime.parse("2023-07-07 14:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        Date openDate = DateTimeUtil.getOpenDate(endDateTime).toDate();
        List<Stock4MinuteDomain> data = stockRtInfoMapper.getStockScreenTimeSharing(openDate, endDate, stockCode);
        //返回数据
        return R.ok(data);
    }

    /*
     * 查询指定股票每天产生的数据，组装成日K线数据
     * *//*
    @Override
    public R<List<Stock4DayDomain>> getStockScreenDaySharing(String stockCode) {
        //获取T日最新的股票交易时间点 endTime
        DateTime endDateTime =DateTimeUtil.getLastDate4Stock(DateTime.now());//T日的截止时间点
        endDateTime =DateTime.parse("2023-01-07 14:55:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate =endDateTime.toDate();
        //获取开始时间
        DateTime startDateTime =endDateTime.minusDays(10);
        startDateTime=DateTime.parse("2022-01-06 09:30:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date startDate =startDateTime.toDate();
        //2.调用mapper 获取指定日期范围内的日K数据
        List<Stock4DayDomain> dkLineData =stockRtInfoMapper.getStockScreenDaySharing(startDate,endDate,stockCode);
        //返回数据
        return R.ok(dkLineData);
    }*/
    /*
     * 个股日K数据查询
     * */
    @Override
    public R<List<Stock4DayDomain>> getStockScreenDaySharing(String stockCode) {
        //获取T日最新的股票交易时间点 endTime
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());//T日的截止时间点
        endDateTime = DateTime.parse("2023-01-07 14:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        //获取开始时间
        DateTime startDateTime = endDateTime.minusDays(10);
        startDateTime = DateTime.parse("2023-01-06 09:50:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date startDate = startDateTime.toDate();
        ////2.调用mapper 获取指定日期范围内的日K数据
        //List<Stock4EvrDayDomain> dkLineData =stockRtInfoMapper.getStockScreenDKLine(startDate,endDate,stockCode);
        //3.方案二:分步实现
        List<Date> mxTimes = stockRtInfoMapper.getMxTime4EvryDay(stockCode, startDate, endDate);
        List<Stock4DayDomain> infos = stockRtInfoMapper.getStock4Dkline2(stockCode, mxTimes);
        if (CollectionUtils.isEmpty(infos)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        //返回数据
        return R.ok(infos);
    }

    /**
     * 根据股票代码模糊查询股票信息
     *
     * @param searchStr 搜索字符串（股票代码）
     * @return
     */
    @Override
    public R<List<StockCodeSearchDomain>> searchStockByCode(String searchStr) {
        //1.包含汉字的处理方法
        if (containsChinese(searchStr)) {
            log.error("搜索参数包含汉字，不支持处理！参数：{}", searchStr);
            return R.error(ResponseCode.DATA_ERROR);
        }
        //2.非汉字参数的常规校验
        if (searchStr == null || searchStr.trim().isEmpty()) {
            log.warn("搜索参数为空，返回空结果");
            return R.error(ResponseCode.DATA_ERROR);
        }
        String trimmedStr = searchStr.trim();
        //3.执行查询逻辑
        try {
            List<StockCodeSearchDomain> data = stockRtInfoMapper.searchStockByCode(trimmedStr);
            log.info("搜索参数：{}，查询到{}条结果", trimmedStr, data.size());
            return R.ok(data);
        } catch (Exception e) {
            log.error("搜索股票失败，参数：{}，错误原因：{}", trimmedStr, e.getMessage(), e);
            return R.error(ResponseCode.ERROR);
        }
    }
    /*
     * 根据股票代码 查询个股描述
     * */
    @Override
    public R<StockDescribeDomain> getStockDescribe(String code) {
        StockDescribeDomain data = stockBusinessMapper.getStockDescribe(code);
        return R.ok(data);
    }

    /*
     * 查询指定股票每周产生的数据，组装成周K线数据
     * */
    @Override
    public R<List<Stock4WeekDomain>> getStockScreenWeekLine(String stockCode) {
        //获取T日最新的股票交易时间点 endTime
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());//T日的截止时间点
        endDateTime = DateTime.parse("2023-01-07 14:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        //获取开始时间
        DateTime startDateTime = endDateTime.minusDays(10);
        startDateTime = DateTime.parse("2023-01-06 09:50:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date startDate = startDateTime.toDate();
        //2.调用mapper 获取指定日期范围内的日K数据
        List<Stock4WeekDomain> wkLineData =stockRtInfoMapper.getStockScreenWKLine(startDate,endDate,stockCode);
        if (CollectionUtils.isEmpty(wkLineData)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        //返回数据
        return R.ok(wkLineData);
    }

    /*
     * 个股最新分时行情
     * */
    @Override
    public R<Stock4SecondDetailDomain> getStockScreenSecondDetail(String stockCode) {
        //1.获取股票最新交易时间点
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());//T日的截止时间点
        endDateTime = DateTime.parse("2023-07-07 14:55:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        Date openDate = DateTimeUtil.getOpenDate(endDateTime).toDate();
        Stock4SecondDetailDomain data = stockRtInfoMapper.getStockScreenSecondDetail(openDate, endDate, stockCode);
        //返回数据
        return R.ok(data);
    }

    @Override
    public R<List<Stock4SecondStatementDomain>> getStockScreenSecondStatement(String stockCode) {
        //1.获取股票最新交易时间点(精确到分钟； 秒 毫秒置为0 因为这样做等值查询效率会更高)
        //DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //因为没有最新数据 所以curDate等后续完成股票采集就可以了 再将其删除
        //Date curDate = DateTimeUtil.getLastDate4Stock(DateTime.now()).toDate();
        //curDate = DateTime.parse("2022-12-28 14:59:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        //Date curDate =curDateTime.toDate();
        List<Stock4SecondStatementDomain> data =stockRtInfoMapper.getStockScreenSecondStatement(stockCode);
        if (CollectionUtils.isEmpty(data)) {
            return R.error(ResponseCode.NO_RESPONSE_DATA);
        }
        //返回数据
        return R.ok(data);
    }

    /*@Override
    public List<StockRtInfo> getStockScreenDayKLine(String stockCode, Date startDate, Date endDate) {
        List<Date> maxTimeList = stockRtInfoMapper.getStockMaxTimeEvrDay(stockCode, startDate, endDate);
        List<StockRtInfo> result = stockRtInfoMapper.selectByMaxTimes(stockCode, maxTimeList);
        return R.ok(result);
    }*/


    private boolean containsChinese(String str) {
        //null视为不包含汉字
        if (str == null) {
            return false;
        }
        // 遍历字符串
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 判断字符是否在汉字Unicode范围内
            if (c >= CHINESE_MIN && c <= CHINESE_MAX) {
                return true; // 找到一个汉字就返回true
            }
        }
        // 遍历完所有字符都没有汉字，返回false
        return false;
    }
}
