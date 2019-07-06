package com.lihao.stock.spider.impl;

import com.lihao.stock.object.HistoryObject;
import com.lihao.stock.object.StockObject;
import com.lihao.stock.service.impl.HistoryServiceImpl;
import com.lihao.stock.spider.SpiderHistoryService;
import com.lihao.stock.util.http.OkHttp;
import com.lihao.stock.util.http.OkHttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SpiderHistoryServiceImpl implements SpiderHistoryService {

    @Autowired
    HistoryServiceImpl historyServiceImpl;
    private final static String historyInfoUrlTemplate = "http://data.gtimg.cn/flashdata/hushen/daily/%s/%s.js?maxage=43201";

    public void getHistoryInfos(List<StockObject> stockObjectList) {
        for (StockObject stockObject : stockObjectList) {
            List<HistoryObject> singleHistoryObjectList = new ArrayList<>();
            singleHistoryObjectList = getHistoryInfo(stockObject);
            if (singleHistoryObjectList.size() != 0) {
                historyServiceImpl.insertHistorys(singleHistoryObjectList);
            }
        }

    }

    private List<HistoryObject> getHistoryInfo(StockObject stockObject) {
        List<HistoryObject> historyObjectList = new ArrayList<>();
        int currentYear = SpiderHistoryService.getSysYear();
        for (int i = 0; i <= 9; i++) {
            String url = String.format(historyInfoUrlTemplate, currentYear - i, stockObject.getStockId());
            System.out.println(url);
            OkHttpResult okHttpResult = OkHttp.doGet(url);
            if (okHttpResult.getCode() == 200) {
                historyObjectList.addAll(getHistoryInfosExp(okHttpResult.getContent(), stockObject));
            }
            try {
                System.out.println(stockObject.getStockName() + "---------sleep");
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return historyObjectList;
    }

    private List<HistoryObject> getHistoryInfosExp(String html, StockObject stockObject) {
        System.out.println(html);
        List<HistoryObject> historyObjectList = new ArrayList<>();
        String[] infos = html.trim().split("\\\\n\\\\\n");
        for(int i=0;i<infos.length-1;i++){
            if(i==0){
                continue;
            }
            String[] infoDetails = infos[i].split(" ");
            Date openingDate = SpiderHistoryService.stringToDate(infoDetails[0],"yyMMdd");
            //开盘价格
            Double openPrice = Double.valueOf(infoDetails[1]);
            //最高价格
            Double maxPrice = Double.valueOf(infoDetails[2]);
            //最低价格
            Double closePrice = Double.valueOf(infoDetails[3]);
            //收盘价格
            Double minPrice = Double.valueOf(infoDetails[4]);
            //交易数量
            int tradeAmount = Integer.valueOf(infoDetails[5]);
            //交易金额
            HistoryObject historyObject = new HistoryObject(stockObject.getStockName(), stockObject.getStockId(), openingDate, openPrice, closePrice, maxPrice, minPrice, tradeAmount);
            historyObjectList.add(historyObject);
        }
        System.out.println(historyObjectList);
        return historyObjectList;
    }



}
