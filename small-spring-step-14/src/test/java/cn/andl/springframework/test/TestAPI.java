package cn.andl.springframework.test;

import cn.andl.springframework.context.support.ClassPathXmlApplicationContext;
import cn.andl.springframework.test.bean.UserService;
import org.junit.Test;

public class TestAPI {

    @Test
    public void test_properties() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-properties.xml");
        UserService userService = context.getBean("userService", UserService.class);
        System.out.println(userService);
    }

    @Test
    public void test_scan() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-scan.xml");
        UserService userService = context.getBean("userService", UserService.class);
        System.out.println(userService.queryUserInfo());
    }

}
