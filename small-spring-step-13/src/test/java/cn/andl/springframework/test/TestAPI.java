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
    public void test_aop() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IUserService userService = classPathXmlApplicationContext.getBean("userService", IUserService.class);
        System.out.println(userService.queryUserInfo());
    }
}
