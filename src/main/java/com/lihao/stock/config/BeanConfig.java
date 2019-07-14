package com.lihao.stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class BeanConfig {

    @Bean(name = "threadPool")
    public ThreadPoolExecutor getThreadPool(){
        System.out.println("初始化核心数5，最大数10，30秒闲置时间的线程池bean");
        return new ThreadPoolExecutor(5,10,30, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
    }
}
