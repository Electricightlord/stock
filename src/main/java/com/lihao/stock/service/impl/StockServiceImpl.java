package com.lihao.stock.service.impl;

import com.lihao.stock.mapper.StockMapper;
import com.lihao.stock.object.StockObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<StockObject> getAllStocks(){
        return stockMapper.getStocks();
    }

    public int getCount(){
        return stockMapper.getCount();
    }

    public String getConcactIds(){
        return stockMapper.getConcatIds();
    }
}
