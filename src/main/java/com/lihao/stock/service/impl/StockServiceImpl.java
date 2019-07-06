package com.lihao.stock.service.impl;

import com.lihao.stock.mapper.StockMapper;
import com.lihao.stock.object.StockObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.lihao.stock.service.StockService;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    StockMapper stockMapper;

    public void insertStocks(List<StockObject> stockObjectList){
        stockMapper.insertStocks(stockObjectList);
    }

    @Cacheable(value = "stock",key = "'allStock'")
    public List<StockObject> getAllStocks(){
        return stockMapper.getStocks();
    }

    @Cacheable(value ="stock",key = "'count'")
    public int getCount(){
        return stockMapper.getCount();
    }

    @Cacheable(value = "stock" ,key = "'ConcatIds'")
    public String getConcactIds(){
        return stockMapper.getConcatIds();
    }
}
