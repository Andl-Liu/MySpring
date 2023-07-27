package cn.andl.springframework.context.event;

/**
 * 容器关闭事件
 */
public class ContextClosedEvent extends ApplicationContextEvent {

    public ContextClosedEvent(Object source) {
        super(source);
    }
}
