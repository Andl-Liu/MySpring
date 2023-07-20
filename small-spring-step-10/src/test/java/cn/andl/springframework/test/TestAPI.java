package cn.andl.springframework.test;

import cn.andl.springframework.beans.context.support.ClassPathXmlApplicationContext;
import cn.andl.springframework.test.event.CustomEvent;
import org.junit.Test;

public class TestAPI {
    @Test
    public void text_event() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        classPathXmlApplicationContext.publishEvent(new CustomEvent(classPathXmlApplicationContext, 123L, "自定义事件"));
        classPathXmlApplicationContext.close();
    }
}
