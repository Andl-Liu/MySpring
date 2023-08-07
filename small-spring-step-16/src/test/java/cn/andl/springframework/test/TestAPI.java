package cn.andl.springframework.test;

import cn.andl.springframework.context.support.ClassPathXmlApplicationContext;
import cn.andl.springframework.test.bean.Husband;
import cn.andl.springframework.test.bean.Wife;
import org.junit.Test;

import java.lang.reflect.Proxy;

public class TestAPI {

    @Test
    public void test_circle() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Husband husband = classPathXmlApplicationContext.getBean("husband", Husband.class);
        Wife wife = classPathXmlApplicationContext.getBean("wife", Wife.class);

        System.out.println(husband.queryWife());

        System.out.println(wife.queryHusband());

    }

    @Test
    public void test_proxy() {
        Object test = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{},
                (proxy, method, args) -> {
                    System.out.println("test");
                    return null;
                });

    }
}
