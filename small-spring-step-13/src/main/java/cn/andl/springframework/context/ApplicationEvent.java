package cn.andl.springframework.context;

import java.util.EventObject;

/**
 * 继承EventObject,定义出具有事件功能的ApplicationEvent类
 */
public abstract class ApplicationEvent extends EventObject {


    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
