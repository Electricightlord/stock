package com.lihao.stock.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lihao.stock.mapper.HistoryMapper;
import com.lihao.stock.object.HistoryObject;
import com.lihao.stock.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    HistoryMapper historyMapper;

    public void insertHistorys(List<HistoryObject> historyObjectList){
        historyMapper.insertHistorys(historyObjectList);
    }

    public PageInfo<HistoryObject> findByStockId(String stockId, int size, int page){
        PageHelper.startPage(page,size);
        return new PageInfo<>(historyMapper.findByStockId(stockId,page,size));
    }

    public List<Map<String,Object>> getCount(){
        return historyMapper.getCount();
    }

}
