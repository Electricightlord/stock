package com.lihao.stock.config;

import com.lihao.stock.job.StockCurrentSynJob;
import com.lihao.stock.job.StockHistoryWriteJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class QuartzConfig {
    private static final int TIME=600;

    @Bean
    public JobDetail stockCurrentSynJobDetail(){
        return JobBuilder.newJob(StockCurrentSynJob.class).withIdentity("stockCurrentSynJob").storeDurably().build();
    }

    @Bean
    public Trigger stockCurrentSynJobTrigger(){
        SimpleScheduleBuilder scheduleBuilder=SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(TIME).repeatForever();
        return TriggerBuilder.newTrigger().forJob(stockCurrentSynJobDetail()).withIdentity("stockCurrentSynTrigger").withSchedule(scheduleBuilder).build();
    }

    @Bean
    public JobDetail stockHistoryWriteJobDetail(){
        return JobBuilder.newJob(StockHistoryWriteJob.class).withIdentity("stockHistoryWriteJob").storeDurably().build();
    }

    @Bean
    public Trigger stockHistoryWriteJobTrigger(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date startTime=new Date();
        try {
            startTime = simpleDateFormat.parse("2019-07-04 18:04");
            System.out.println("初始化时间"+startTime.toString());
        }catch (Exception e){
            System.out.println("时间初始化异常");
        }
        SimpleScheduleBuilder scheduleBuilder=SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(TIME).repeatForever();
        return TriggerBuilder.newTrigger().forJob(stockHistoryWriteJobDetail()).withIdentity("stockHistoryWriteTrigger").startAt(startTime).withSchedule(scheduleBuilder).build();
    }
}
