package com.lihao.stock.mapper;

import com.lihao.stock.object.StockObject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StockMapper {
     @Cacheable(value = "stock" ,keyGenerator = "keyGenerator")
     void insertStocks(List<StockObject> stockObjectList);

     @Cacheable(value = "stock" ,keyGenerator = "keyGenerator")
     List<StockObject> getStocks();

     int getCount();

     @Cacheable(value = "stock",key = "'ConcatIds'")
     String getConcatIds();
}
