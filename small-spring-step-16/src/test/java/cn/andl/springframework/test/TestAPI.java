package cn.andl.springframework.test;

import cn.andl.springframework.context.support.ClassPathXmlApplicationContext;
import cn.andl.springframework.test.bean.IUserService;
import cn.andl.springframework.test.bean.UserService;
import org.junit.Test;

public class TestAPI {

    @Test
    public void test_aop() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring2.xml");
        IUserService userService = context.getBean("userService", IUserService.class);
        System.out.println(userService.queryUserInfo());
    }

}
