package cn.andl.springframework.context;

import cn.andl.springframework.beans.BeansException;

/**
 * 可配置的应用上下文接口
 * 提供刷新容器的机制
 */
public interface ConfigurableApplicationContext extends ApplicationContext {

    /**
     * 刷新容器
     */
    void refresh() throws BeansException;

    /**
     * 注册虚JVM的ShutdownHook
     */
    void registerShutdownHook();

    /**
     * 关闭应用上下文时执行的方法
     */
    void close();
}
