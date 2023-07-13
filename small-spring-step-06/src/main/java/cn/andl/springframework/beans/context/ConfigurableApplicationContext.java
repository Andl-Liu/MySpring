package cn.andl.springframework.beans.context;

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

}
