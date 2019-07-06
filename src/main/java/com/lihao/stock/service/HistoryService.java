package com.lihao.stock.service;

import com.github.pagehelper.PageInfo;
import com.lihao.stock.object.HistoryObject;

import java.util.List;
import java.util.Map;

public interface HistoryService {
     void insertHistorys(List<HistoryObject> historyObjectList);

     PageInfo<HistoryObject> findByStockId(String stockId, int size, int page);

     List<Map<String,Object>> getCount();
}
