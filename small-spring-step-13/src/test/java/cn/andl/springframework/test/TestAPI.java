package cn.andl.springframework.test;

import cn.andl.springframework.aop.AdvisedSupport;
import cn.andl.springframework.aop.TargetSource;
import cn.andl.springframework.aop.aspectj.AspectJExpressionPointcut;
import cn.andl.springframework.aop.framework.Cglib2AopProxy;
import cn.andl.springframework.aop.framework.JdkDynamicAopProxy;
import cn.andl.springframework.beans.context.support.ClassPathXmlApplicationContext;
import cn.andl.springframework.test.bean.IUserService;
import cn.andl.springframework.test.bean.MethodServiceInterceptor;
import cn.andl.springframework.test.bean.UserService;
import org.junit.Test;

import java.lang.reflect.Method;

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
