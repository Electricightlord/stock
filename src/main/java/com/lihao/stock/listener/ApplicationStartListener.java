package com.lihao.stock.listener;

import com.lihao.stock.object.StockObject;
import com.lihao.stock.spider.impl.SpiderHistoryServiceImpl;
import com.lihao.stock.spider.impl.SpiderStockServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        System.out.println("bean初始化完成，开始同步数据");
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        SpiderStockServiceImpl spiderStockService = applicationContext.getBean(SpiderStockServiceImpl.class);
        SpiderHistoryServiceImpl spiderHistoryService = applicationContext.getBean(SpiderHistoryServiceImpl.class);
        RedisTemplate<String, Object> stringObjectRedisTemplate = (RedisTemplate<String, Object>) applicationContext.getBean("objectTemplate");

        boolean result = spiderStockService.getStockInfos();
        result=false;
        if (result) {
            SynStockHisInfo synStockHisInfo = new SynStockHisInfo(spiderHistoryService, stringObjectRedisTemplate);
            for (int i = 0; i < 5; i++) {
                Thread thread = new Thread(synStockHisInfo);
                thread.start();
            }
        }
    }
}

class SynStockHisInfo extends Thread {

    //开始索引
    private int startIndex;
    //每一次同步数量
    private int syncCountPerTime;
    //已经同步数量
    volatile public int totalSyncCount;
    //股票信息集合
    private List<StockObject> stockObjectList;
    //ids集合大小
    private int size;

    private SpiderHistoryServiceImpl spiderHistoryService;

    public SynStockHisInfo(SpiderHistoryServiceImpl spiderHistoryService, RedisTemplate<String, Object> stringObjectRedisTemplate) {
        this.spiderHistoryService = spiderHistoryService;
        this.stockObjectList = (List<StockObject>) stringObjectRedisTemplate.opsForValue().get("allStock");
        if (stockObjectList == null) {
            this.size = 0;
        }
        this.size = stockObjectList.size();
        this.startIndex = 0;
        this.syncCountPerTime = 10;
        this.totalSyncCount = 0;

    }

    public boolean doSyn(List<StockObject> stockObjectList) {
        if (stockObjectList == null) {
            return true;
        }

        int startIndexCurrentThread;
        int endIndex;
        //TODO 获得当前线程需要同步的股票信息
        synchronized (this) {
            if (totalSyncCount >= size) {
                return true;
            }
            startIndex = this.totalSyncCount;
            endIndex = startIndex + syncCountPerTime >= size ? size : startIndex + syncCountPerTime;
            totalSyncCount = endIndex;
            startIndexCurrentThread = startIndex;
        }

            spiderHistoryService.getHistoryInfos(stockObjectList.subList(startIndexCurrentThread, endIndex));
        return false;
    }

    @Override
    public void run() {
        if (this.size == 0) {
            return;
        }
        boolean isDone = false;
        while (!isDone) {
            isDone = doSyn(stockObjectList);
        }
    }
}
