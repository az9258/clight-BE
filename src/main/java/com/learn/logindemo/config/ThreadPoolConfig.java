package com.learn.logindemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池配置类
 * <p>
 * 该类用于定义 Spring 管理的线程池 Bean，用于处理异步任务或定时任务。
 * 通过配置线程池，可以控制线程的数量、任务队列的大小等，从而优化系统性能。
 */
@Configuration
public class ThreadPoolConfig {
    @Bean
    public ExecutorService taskExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}
