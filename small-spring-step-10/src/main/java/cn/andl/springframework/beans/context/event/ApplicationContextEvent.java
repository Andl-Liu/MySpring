package cn.andl.springframework.beans.context.event;

import cn.andl.springframework.beans.context.ApplicationContext;
import cn.andl.springframework.beans.context.ApplicationEvent;

/**
 * 定义容器事件的抽象类，所有的事件（刷新、关闭和用户自定义的事件）都需要继承这个类
 */
public class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(Object source) {
        super(source);
    }

    public ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
