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

//    @Cacheable(value = "history",key = "T(String).valueOf(#stockId).concat('::')+T(String).valueOf(#page).concat('::')+T(String).valueOf(#size)")
    List<HistoryObject> findByStockId(String stockId,int page,int size);

//    @Cacheable(value="history",key = "count")
    List<Map<String,Object>> getCount();
}
