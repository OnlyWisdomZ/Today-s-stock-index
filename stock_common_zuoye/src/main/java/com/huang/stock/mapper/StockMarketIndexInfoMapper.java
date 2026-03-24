package com.huang.stock.mapper;

import com.huang.stock.pojo.domain.InnerMarketDomain;
import com.huang.stock.pojo.entity.StockMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author OnlyWisdomZ
* @description 针对表【stock_market_index_info(国内大盘数据详情表)】的数据库操作Mapper
* @createDate 2025-07-19 11:44:12
* @Entity com.huang.stock.pojo.entity.StockMarketIndexInfo
*/
public interface StockMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockMarketIndexInfo record);

    int insertSelective(StockMarketIndexInfo record);

    StockMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockMarketIndexInfo record);

    int updateByPrimaryKey(StockMarketIndexInfo record);

    /*
     * 获取国内大盘最新数据
     * */
    List<InnerMarketDomain> getMarketInfo(@Param("curDate") Date curDate, @Param("mCodes") List<String> mCodes);

    /*
     * 统计国内大盘T日和T-1日成交量对比功能
     * */
    List<Map> getSumAmtInfo(@Param("openDate") Date openDate, @Param("endDate") Date endDate, @Param("marketCodes") List<String> marketCodes);

    /*
    * 调用mybatis批量入库大盘数据
    * @param entities 大盘实体数据集合
    * */
    int insertBatch(@Param("entities") ArrayList<StockMarketIndexInfo> entities);

}
