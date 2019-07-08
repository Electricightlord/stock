package com.lihao.stock.config;

import com.lihao.stock.job.StockCurrentSynJob;
import com.lihao.stock.job.StockHistoryWriteJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    private static final int TIME=10;

    @Bean
    public JobDetail stockCurrentSynJobDetail(){
        return JobBuilder.newJob(StockCurrentSynJob.class).withIdentity("stockCurrentSynJob").storeDurably().build();
    }

    @Bean
    public Trigger stockCurrentSynJobTrigger(){
        SimpleScheduleBuilder scheduleBuilder=SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(TIME).repeatForever();
        return TriggerBuilder.newTrigger().forJob(stockCurrentSynJobDetail()).withIdentity("stockCurrentSynTrigger").startAt(DateBuilder.futureDate(5, DateBuilder.IntervalUnit.MINUTE)).withSchedule(scheduleBuilder).build();
    }

    @Bean
    public JobDetail stockHistoryWriteJobDetail(){
        return JobBuilder.newJob(StockHistoryWriteJob.class).withIdentity("stockHistoryWriteJob").storeDurably().build();
    }

    @Bean
    public Trigger stockHistoryWriteJobTrigger(){
        return TriggerBuilder.newTrigger().forJob(stockHistoryWriteJobDetail()).withIdentity("stockHistoryWriteTrigger").withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0,0)).build();
    }
}
