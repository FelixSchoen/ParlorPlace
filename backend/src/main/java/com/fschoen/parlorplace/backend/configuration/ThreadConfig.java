package com.fschoen.parlorplace.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadConfig {

    @Bean
    public TaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setThreadNamePrefix("task_executor_thread");
        executor.initialize();

        return executor;
    }

}
