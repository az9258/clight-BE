/**
 * 线程池配置类，用于配置并创建一个 Spring 管理的线程池。
 * 该类使用 Spring 的配置注解，将线程池作为一个 Bean 注入到 Spring 容器中。
 */
package site.clight.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
/**
 * 线程池配置类，负责创建并配置一个固定大小的线程池，并将其作为 Bean 注入到 Spring 容器中。
 */
public class ThreadPoolConfig {
    /**
     * 定义一个 Spring Bean，名为 taskExecutor，用于提供一个固定大小的线程池。
     * 该线程池的大小为 10，即最多同时执行 10 个线程任务。
     *
     * @return 一个固定大小为 10 的线程池服务实例。
     */
    @Bean
    public ExecutorService taskExecutor() {
        // 创建一个固定大小为 10 的线程池并返回
        return Executors.newFixedThreadPool(10);
    }
}
