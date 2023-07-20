package cn.andl.springframework.test.event;

import cn.andl.springframework.beans.context.event.ContextRefreshedEvent;
import cn.andl.springframework.beans.context.support.ApplicationListener;

public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("刷新事件：" + this.getClass().getName());
    }

}
