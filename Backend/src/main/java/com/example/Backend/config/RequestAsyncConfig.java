package com.example.Backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
@EnableAsync
public class RequestAsyncConfig {
    @Bean(name="MultiRequestAsyncThread")
    public Executor getThreadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setThreadNamePrefix("RequestThread:- ");
        executor.initialize();
        return executor;
    }
}
