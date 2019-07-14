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

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

public class StockHistoryWriteJob extends QuartzJobBean {

    @Autowired
    StockServiceImpl stockService;

    @Autowired
    RedisTemplate<String, Object> stringObjectRedisTemplate;

    @Autowired
    HistoryServiceImpl historyServiceImpl;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext)  {
        System.out.println("开始写入数据库");
        List<StockObject> stockObjectList = stockService.getAllStocks();
        System.out.println("需要写入的数据条数:"+stockObjectList.size());
        CountDownLatch countDownLatch=new CountDownLatch(5);
        WriteHistory syncJob = new WriteHistory(stockObjectList, 0, 50, 0, stringObjectRedisTemplate,countDownLatch);
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(syncJob);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<HistoryObject> historyObjectList=syncJob.getHistoryObjectList();
        System.out.println("写入数据库的信息条数:"+historyObjectList.size());
        historyServiceImpl.insertHistorys(historyObjectList);
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

    private CountDownLatch countDownLatch;


    private Vector<HistoryObject> historyObjectList = new Vector<>();

    public WriteHistory(List<StockObject> stockObjectList, int startIndex, int writeCountPerTime, int totalWriteCount, RedisTemplate<String, Object> stringObjectRedisTemplate,CountDownLatch countDownLatch) {
        super();
        this.stockObjectList = stockObjectList;
        this.startIndex = startIndex;
        this.writeCountPerTime = writeCountPerTime;
        this.totalWriteCount = totalWriteCount;
        this.size = stockObjectList.size();
        this.stringObjectRedisTemplate = stringObjectRedisTemplate;
        this.countDownLatch=countDownLatch;
    }

    @Override
    public void run() {
        boolean isDone = false;
        while (!isDone) {
            isDone = doSyncJob();
        }
        countDownLatch.countDown();
    }

    public boolean doSyncJob() {
        int startIndexCurrentThread;
        int endIndex;
        //TODO 获得当前线程需要同步的股票信息
        synchronized (this) {
            if (totalWriteCount >= size) {
                return true;
            }
            startIndex = this.totalWriteCount;
            endIndex = startIndex + writeCountPerTime >= size ? size : startIndex + writeCountPerTime;
            totalWriteCount = endIndex;
            startIndexCurrentThread=startIndex;
        }
        //TODO 开始同步信息
        doJob(stockObjectList.subList(startIndexCurrentThread, endIndex));
        return false;
    }

    private void doJob(List<StockObject> stockObjectList) {
        for (StockObject stockObject : stockObjectList) {
            CurrentObject currentObject = (CurrentObject) stringObjectRedisTemplate.opsForValue().get(stockObject.getStockId());
            if (currentObject == null) {
                return;
            }
            HistoryObject historyObject = currentObject.toHistoryObject();
            historyObjectList.add(historyObject);
        }
    }
}
