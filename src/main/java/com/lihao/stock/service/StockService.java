package com.lihao.stock.service;

import com.lihao.stock.object.StockObject;

import java.util.List;

public interface StockService {

     void insertStocks(List<StockObject> stockObjectList);

     List<StockObject> getAllStocks();

     int getCount();

     String getConcactIds();
}
