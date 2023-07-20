package cn.andl.springframework.test.event;

import cn.andl.springframework.beans.context.event.ContextClosedEvent;
import cn.andl.springframework.beans.context.support.ApplicationListener;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("关闭事件：" + this.getClass().getName());
    }

}
