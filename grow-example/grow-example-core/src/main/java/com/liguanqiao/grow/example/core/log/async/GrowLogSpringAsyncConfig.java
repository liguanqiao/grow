package com.liguanqiao.grow.example.core.log.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author liguanqiao
 * @since 2023/2/15
 **/
@Configuration
public class GrowLogSpringAsyncConfig {


    @Bean("chatGptAsyncTaskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor() {
        // 此方法返回可用处理器的虚拟机的最大数量; 不小于1
        int core = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
        // 设置最大线程数
        asyncTaskExecutor.setMaxPoolSize(core * 5);
        // 设置核心线程数
        asyncTaskExecutor.setCorePoolSize(core + 1);
        asyncTaskExecutor.setThreadNamePrefix("chat-gpt-async-task-thread-");
        // 如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        asyncTaskExecutor.setQueueCapacity(500);
        /*
         * 拒绝策略
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        asyncTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        asyncTaskExecutor.initialize();
        return asyncTaskExecutor;
    }

}
