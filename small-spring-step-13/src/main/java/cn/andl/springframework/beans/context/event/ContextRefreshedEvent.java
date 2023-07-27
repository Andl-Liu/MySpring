package cn.andl.springframework.beans.context.event;

/**
 * 容器刷新事件
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {

    public ContextRefreshedEvent(Object source) {
        super(source);
    }
}
