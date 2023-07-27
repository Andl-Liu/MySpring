package cn.andl.springframework.beans.context.event;

import cn.andl.springframework.beans.context.ApplicationEvent;
import cn.andl.springframework.beans.context.support.ApplicationListener;

/**
 * ApplicationEvent广播器接口
 */
public interface ApplicationEventMulticaster {

    /**
     * 添加一个容器事件监听器
     * @param applicationListener 目标容器事件监听器
     */
    void addApplicationListener(ApplicationListener<?> applicationListener);

    /**
     * 删除一个容器事件监听器
     * @param applicationListener 目标容器事件监听器
     */
    void removeApplicationListener(ApplicationListener<?> applicationListener);

    /**
     * 把接收的事件广播给合适的事件监听器
     * @param applicationEvent 事件
     */
    void multicastEvent(ApplicationEvent applicationEvent);

}
