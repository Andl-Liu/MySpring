package cn.andl.springframework.context;

/**
 * 容器事件发布器接口
 */
public interface ApplicationEventPublisher {

    /**
     * 发布目标事件，通知所有监听目标事件的监听器
     * @param event 目标事件
     */
    void publishEvent(ApplicationEvent event);

}
