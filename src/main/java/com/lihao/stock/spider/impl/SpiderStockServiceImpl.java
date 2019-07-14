package com.lihao.stock.spider.impl;

import com.lihao.stock.object.StockObject;
import com.lihao.stock.service.impl.StockServiceImpl;
import com.lihao.stock.spider.SpiderStockService;
import com.lihao.stock.util.http.OkHttp;
import com.lihao.stock.util.http.OkHttpResult;
import com.lihao.stock.util.xpath.NXpathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpiderStockServiceImpl implements SpiderStockService {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    StockServiceImpl stockService;

    private final static String stockInfoUrl = "http://quote.stockstar.com/stock/stock_index.htm";

    public boolean getStockInfos() {
        List<StockObject> stockObjectList = new ArrayList<>();
        OkHttpResult okHttpResult = OkHttp.doGet(stockInfoUrl);
        if (okHttpResult.getCode() == 200) {
            stockObjectList.addAll(getStockInfosExp(okHttpResult.getContent()));
        }
        if(stockObjectList.size()!=0) {
            stockService.insertStocks(stockObjectList);
            redisTemplate.opsForValue().set("allStock",stockObjectList);
            System.out.println("股票信息下载完成");
            return true;
        }
        return false;
}

    private List<StockObject> getStockInfosExp(String html) {
        String[] expArray={"//*[@id=\"index_data_0\"]/li","//*[@id=\"index_data_1\"]/li","//*[@id=\"index_data_2\"]/li","//*[@id=\"index_data_3\"]/li"};
        List<StockObject> stockObjectList = new ArrayList<>();
        for(int i=0;i<4;i++) {
            Object result = NXpathUtil.getValues(html, expArray[i]);
            try {
                if (result instanceof NodeList) {
                    NodeList nodeList = (NodeList) result;
                    int nodeLength = nodeList.getLength();
                    for (int j = 0; j < nodeLength; j++) {
                        StockObject stockObject=new StockObject();
                        Node node = nodeList.item(j);
                        //stockId
                        String stockId = ((String) NXpathUtil.xPath.evaluate("span/a", node, XPathConstants.STRING));
                        //名称
                        String stockName = (String) NXpathUtil.xPath.evaluate("a", node, XPathConstants.STRING);
                        if(i<2) {
                            stockObject.setStockId("sh"+stockId);
                        }else{
                            stockObject.setStockId("sz"+stockId);
                        }
                        stockObject.setStockName(stockName);
                        stockObjectList.add(stockObject);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stockObjectList;
    }
}
