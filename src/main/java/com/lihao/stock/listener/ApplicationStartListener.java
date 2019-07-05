package com.lihao.stock.listener;

import com.lihao.stock.object.StockObject;
import com.lihao.stock.service.spider.SpiderHistoryService;
import com.lihao.stock.service.spider.SpiderStockService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        SynStockHisInfo synStockHisInfo=new SynStockHisInfo(contextRefreshedEvent);
        synStockHisInfo.start();
    }
}

class SynStockHisInfo extends Thread{

    private ContextRefreshedEvent contextRefreshedEvent;

    public SynStockHisInfo(ContextRefreshedEvent contextRefreshedEvent){
        this.contextRefreshedEvent=contextRefreshedEvent;
    }

    public void doSyn(){
        System.out.println("开始同步数据");
        ApplicationContext applicationContext=this.contextRefreshedEvent.getApplicationContext();
        SpiderStockService spiderStockService=applicationContext.getBean(SpiderStockService.class);
        SpiderHistoryService spiderHistoryService=applicationContext.getBean(SpiderHistoryService.class);
        RedisTemplate<String,Object> stringObjectRedisTemplate=(RedisTemplate<String,Object>)applicationContext.getBean("objectTemplate");
        boolean result=spiderStockService.getStockInfos();
        System.out.println(result);
        if (result ) {
            List<StockObject> stockObjectList=(List<StockObject>)stringObjectRedisTemplate.opsForValue().get("allStockCache");
            if(stockObjectList==null){
                return;
            }
            int stockCount = stockObjectList.size();
            int start = 0;
            while (start<stockCount) {
                int end=start+5>stockCount?stockCount:start+5;
                spiderHistoryService.getHistoryInfos(stockObjectList.subList(start,end));
                start+=5;
            }
        }
        System.out.println("同步数据完成");
    }
    public void run(){
//        doSyn();
    }
}
