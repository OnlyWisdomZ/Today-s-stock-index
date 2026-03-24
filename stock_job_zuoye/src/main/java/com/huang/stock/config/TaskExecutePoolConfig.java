package com.huang.stock.config;

import com.huang.stock.vo.TaskThreadPoolInfo;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;
@Component
@EnableConfigurationProperties(TaskThreadPoolInfo.class)
public class TaskExecutePoolConfig {
    private TaskThreadPoolInfo info;
    public TaskExecutePoolConfig(TaskThreadPoolInfo info){
        this.info=info;
    }
    @Bean(name="threadPoolTaskExecutor",destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor =new ThreadPoolTaskExecutor();
        //设置核心线程数(线程池创建的时候 初始化的线程数)
        executor.setCorePoolSize(info.getCorePoolSize());
        //设置最大线程数： 只有缓冲队列满了 才会申请超过核心线程数的线程
        executor.setMaxPoolSize(info.getMaxPoolSize());
        //允许线程的空闲时间： 当超过核心线程数之外的线程在空闲时间到达之后才会被销毁
        executor.setKeepAliveSeconds(info.getKeepAliveSeconds());
        //设置任务队列的长度 缓冲队列：用来执行缓冲任务的队列
        executor.setQueueCapacity(info.getQueueCapacity());
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());//终止策略
        //将设置的参数初始化
        executor.initialize();
        return executor;
    }
}
