package com.lihao.stock.job;

import com.lihao.stock.object.CurrentObject;
import com.lihao.stock.service.impl.StockServiceImpl;
import com.lihao.stock.spider.SpiderHistoryService;
import com.lihao.stock.util.http.OkHttp;
import com.lihao.stock.util.http.OkHttpResult;
import lombok.Data;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockCurrentSynJob extends QuartzJobBean {

    @Autowired
    StockServiceImpl stockService;

    @Autowired
    RedisTemplate<String, Object> stringObjectRedisTemplate;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("stockService"+stockService);
        String concactIds=stockService.getConcactIds();
        System.out.println("concactIds:"+concactIds);
        String[] stockIds = concactIds.split(",");
        while (stockIds.length==0){
            try {
                Thread.sleep(30000);
            }catch (Exception e){
                e.printStackTrace();
            }
            stockIds=stockService.getConcactIds().split(",");
        }
        SyncJob syncJob = new SyncJob(stockIds, stringObjectRedisTemplate);
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(syncJob);
        }
    }
}

@Data
class SyncJob extends Thread {
    //开始索引
    private int startIndex;
    //每一次同步数量
    private int syncCountPerTime;
    //已经同步数量
    volatile public int totalSyncCount;
    //ids集合
    private String[] stockIds;
    //ids集合大小
    private int size;

    private RedisTemplate<String, Object> stringObjectRedisTemplate;

    SyncJob(String[] stockIds, RedisTemplate<String, Object> stringObjectRedisTemplate) {
        super();
        this.stockIds = stockIds;
        this.startIndex = 0;
        this.syncCountPerTime = 50;
        this.totalSyncCount = 0;
        this.size = stockIds.length;
        this.stringObjectRedisTemplate = stringObjectRedisTemplate;
    }

    @Override
    public void run() {
        System.out.println("线程id" + Thread.currentThread().getId() + "启动");
        boolean isDone = false;
        while (!isDone) {
            isDone = doSyncJob();
        }
    }

    boolean doSyncJob() {
        int startIndexCurrentThread;
        int endIndex;
        //TODO 获得当前线程需要下载的股票信息
        synchronized (this) {
            if (totalSyncCount >= size) {
                return true;
            }
            startIndex = this.totalSyncCount;
            endIndex = startIndex + syncCountPerTime >= size ? size : startIndex + syncCountPerTime;
            totalSyncCount = endIndex;
            startIndexCurrentThread=startIndex;
        }
        //TODO 开始同步信息
        try {
            doJob(Arrays.copyOfRange(stockIds, startIndexCurrentThread, endIndex));
        } catch (Exception e) {
            System.out.println("线程id" + Thread.currentThread().getId() +"startCurrentIndex:"+startIndexCurrentThread);
        }

        return false;
    }

    private void doJob(String[] stockIds) {
        String param = String.join(",", stockIds);
        String url_template = "http://hq.sinajs.cn/list=%s";
        String request_url = String.format(url_template, param);
        OkHttpResult okHttpResult = OkHttp.doGet(request_url);
        String[] results = okHttpResult.getContent().split("\n");
        Pattern p = Pattern.compile(".*((sz|sh)\\d{6})=\"((.*))\";");
        for (String result : results) {
            Matcher m = p.matcher(result);
            if (m.find()) {
                if (m.groupCount() != 4) {
                    return;
                }
                CurrentObject currentObject = new CurrentObject();
                currentObject.setStockId(m.group(1));
                String[] historyInfo = (m.group(3)).split(",");
                try {
                    currentObject.setStockName(historyInfo[0]);
                    currentObject.setOpenPrice(Double.valueOf(historyInfo[1]));
                    currentObject.setYesterdayPrice(Double.valueOf(historyInfo[2]));
                    currentObject.setCurrentPrice(Double.valueOf(historyInfo[3]));
                    currentObject.setMaxPrice(Double.valueOf(historyInfo[4]));
                    currentObject.setMinPrice(Double.valueOf(historyInfo[5]));
                    currentObject.setTradeAmount(Integer.valueOf(historyInfo[8]));
                    currentObject.setOpeningDate(SpiderHistoryService.stringToDate(historyInfo[30], "yyyy-MM-dd"));
                    stringObjectRedisTemplate.opsForValue().set(currentObject.getStockId(), currentObject);
                } catch (Exception e) {
                    System.out.println("无法获取信息" + result);
                }
            }
        }
    }
}