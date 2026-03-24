package com.huang.stock.mapper;

import com.huang.stock.pojo.domain.OuterMarketDomain;
import com.huang.stock.pojo.entity.StockOuterMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author OnlyWisdomZ
* @description 针对表【stock_outer_market_index_info(外盘详情信息表)】的数据库操作Mapper
* @createDate 2025-07-19 11:44:12
* @Entity com.huang.stock.pojo.entity.StockOuterMarketIndexInfo
*/
public interface StockOuterMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockOuterMarketIndexInfo record);

    int insertSelective(StockOuterMarketIndexInfo record);

    StockOuterMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockOuterMarketIndexInfo record);

    int updateByPrimaryKey(StockOuterMarketIndexInfo record);
    /*
     * 获取国外大盘最新数据
     * */
    List<OuterMarketDomain> getOuterMarketInfo(@Param("curDate") Date curDate, @Param("oCodes") List<String> oCodes);
    /*
     * 调用mybatis批量入库外盘数据
     * */
    int insertBatch(@Param("entities") ArrayList<StockOuterMarketIndexInfo> entities);
}
