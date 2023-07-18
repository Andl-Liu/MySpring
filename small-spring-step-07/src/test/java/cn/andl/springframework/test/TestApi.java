package cn.andl.springframework.test;

import cn.andl.springframework.beans.context.support.ClassPathXmlApplicationContext;
import cn.andl.springframework.test.beans.UserService;
import org.junit.Test;

public class TestApi {

    @Test
    public void test_xml() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        classPathXmlApplicationContext.registerShutdownHook();

        UserService userService = (UserService)classPathXmlApplicationContext.getBean("userService");
        System.out.println(userService.queryUserInfo());
    }

}
