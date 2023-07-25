package cn.andl.springframework.beans.context.support;

import cn.andl.springframework.beans.context.ApplicationEvent;

import java.util.EventListener;

/**
 * 应用监听器接口，
 * @param <E> application
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 处理一个ApplicationEvent
     * @param event applicationEvent
     */
    void onApplicationEvent(E event);

}
