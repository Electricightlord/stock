package com.lihao.stock.job;

import com.lihao.stock.object.CurrentObject;
import com.lihao.stock.service.StockService;
import com.lihao.stock.service.spider.SpiderHistoryService;
import com.lihao.stock.util.http.OkHttp;
import com.lihao.stock.util.http.OkHttpResult;
import lombok.Data;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockCurrentSynJob extends QuartzJobBean {

    @Autowired
    StockService stockService;

    @Autowired
    RedisTemplate<String,Object> stringObjectRedisTemplate;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        String[] stockIds = stockService.getConcactIds().split(",");
        SyncJob syncJob = new SyncJob(stockIds, 0, 50, 0,stringObjectRedisTemplate);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(syncJob);
            thread.setName(String.valueOf(i));
            thread.start();
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

    private RedisTemplate<String,Object> stringObjectRedisTemplate;

    public SyncJob(String[] stockIds, int startIndex, int syncCountPerTime, int totalSyncCount, RedisTemplate<String,Object> stringObjectRedisTemplate) {
        super();
        this.stockIds = stockIds;
        this.startIndex = startIndex;
        this.syncCountPerTime = syncCountPerTime;
        this.totalSyncCount = totalSyncCount;
        this.size = stockIds.length;
        this.stringObjectRedisTemplate=stringObjectRedisTemplate;
        System.out.println(this.size);
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
            if (totalSyncCount >= size) {
                return true;
            }
            startIndex = this.totalSyncCount;
            endIndex = startIndex + syncCountPerTime >= size ? size : startIndex + syncCountPerTime;
            totalSyncCount = endIndex;
        }
        //TODO 同步信息
        doJob(Arrays.copyOfRange(stockIds, startIndex, endIndex));
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
                if (m.groupCount()!=4){
                    return ;
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
                    currentObject.setOpeningDate(SpiderHistoryService.stringToDate(historyInfo[30],"yyyy-MM-dd"));
                    stringObjectRedisTemplate.opsForValue().set(currentObject.getStockId(), currentObject);
                }catch (Exception e){
                    System.out.println(result);
                }
            }
        }
    }
}


