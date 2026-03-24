package com.huang.stock.mapper;

import com.huang.stock.pojo.domain.StockDescribeDomain;
import com.huang.stock.pojo.entity.StockBusiness;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author OnlyWisdomZ
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2025-07-19 11:44:12
* @Entity com.huang.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);
    /*
    * 获取所有的A股编码集合
    * */
    List<String> getStockIds();
    /*
     * 根据股票代码 查询个股描述
     * */
    StockDescribeDomain getStockDescribe(@Param("code") String code);
}
