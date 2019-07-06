package com.lihao.stock.mapper;

import com.lihao.stock.object.HistoryObject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface HistoryMapper {
    void insertHistorys(List<HistoryObject> historyObjectList);

List<HistoryObject> findByStockId(String stockId,int page,int size);


    List<Map<String,Object>> getCount();
}
