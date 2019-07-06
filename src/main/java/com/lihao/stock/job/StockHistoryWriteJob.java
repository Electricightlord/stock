package com.lihao.stock.job;

import com.lihao.stock.object.CurrentObject;
import com.lihao.stock.object.HistoryObject;
import com.lihao.stock.object.StockObject;
import com.lihao.stock.service.impl.HistoryServiceImpl;
import com.lihao.stock.service.impl.StockServiceImpl;
import lombok.Data;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.List;

public class StockHistoryWriteJob extends QuartzJobBean {

    @Autowired
    StockServiceImpl stockService;

    @Autowired
    RedisTemplate<String, Object> stringObjectRedisTemplate;

    @Autowired
    HistoryServiceImpl historyServiceImpl;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext)  {
        System.out.println("开始写入数据库");
        List<StockObject> stockObjectList = stockService.getAllStocks();
        WriteHistory syncJob = new WriteHistory(stockObjectList, 0, 50, 0, stringObjectRedisTemplate);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(syncJob);
            thread.setName(String.valueOf(i));
            thread.start();
            try {
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        historyServiceImpl.insertHistorys(syncJob.getHistoryObjectList());
        System.out.println("数据库写入完成");
    }
}

@Data
class WriteHistory extends Thread {
    //开始索引
    private int startIndex;
    //每一次同步数量
    private int writeCountPerTime;
    //已经同步数量
    volatile public int totalWriteCount;
    //ids集合
    private List<StockObject> stockObjectList;
    //ids集合大小
    private int size;

    private RedisTemplate<String, Object> stringObjectRedisTemplate;


    private List<HistoryObject> historyObjectList = new ArrayList<>();

    public WriteHistory(List<StockObject> stockObjectList, int startIndex, int writeCountPerTime, int totalWriteCount, RedisTemplate<String, Object> stringObjectRedisTemplate) {
        super();
        this.stockObjectList = stockObjectList;
        this.startIndex = startIndex;
        this.writeCountPerTime = writeCountPerTime;
        this.totalWriteCount = totalWriteCount;
        this.size = stockObjectList.size();
        this.stringObjectRedisTemplate = stringObjectRedisTemplate;
    }

    @Override
    public void run() {
        boolean isDown = false;
        while (!isDown) {
            isDown = doSyncJob();
        }

    }

    public boolean doSyncJob() {
        int endIndex;
        synchronized (this) {
            if (totalWriteCount >= size) {
                return true;
            }
            startIndex = this.totalWriteCount;
            endIndex = startIndex + writeCountPerTime >= size ? size : startIndex + writeCountPerTime;
            totalWriteCount = endIndex;
        }
        //TODO 同步信息
        doJob(stockObjectList.subList(startIndex, endIndex));
        return false;
    }

    private void doJob(List<StockObject> stockObjectList) {
        for (StockObject stockObject : stockObjectList) {
            CurrentObject currentObject = (CurrentObject) stringObjectRedisTemplate.opsForValue().get(stockObject.getStockId());
            System.out.println(currentObject);
            if (currentObject == null) {
                return;
            }
            HistoryObject historyObject = currentObject.toHistoryObject();
            historyObjectList.add(historyObject);
        }
    }
}
