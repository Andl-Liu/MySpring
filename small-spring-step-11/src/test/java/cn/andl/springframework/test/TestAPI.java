package cn.andl.springframework.test;

import cn.andl.springframework.aop.AdvisedSupport;
import cn.andl.springframework.aop.TargetSource;
import cn.andl.springframework.aop.aspectj.AspectJExpressionPointcut;
import cn.andl.springframework.aop.framework.Cglib2AopProxy;
import cn.andl.springframework.aop.framework.JdkDynamicAopProxy;
import cn.andl.springframework.test.bean.IUserService;
import cn.andl.springframework.test.bean.MethodServiceInterceptor;
import cn.andl.springframework.test.bean.UserService;
import org.junit.Test;

import java.lang.reflect.Method;

public class TestAPI {

    @Test
    public void test_aop() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* cn.andl.springframework.test.bean.UserService.*(..))");
        Class<UserService> clazz = UserService.class;
        Method method = clazz.getDeclaredMethod("queryUserInfo");

        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method, clazz));

        // true、true
    }

    @Test
    public void test_dynamic() {
        IUserService userService = new UserService();

        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setMethodInterceptor(new MethodServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* cn.andl.springframework.test.bean.IUserService.*(..))"));

        IUserService jdkProxy = (IUserService)new JdkDynamicAopProxy(advisedSupport).getProxy();
        System.out.println(jdkProxy.queryUserInfo());

        IUserService cgLibProxy = (IUserService) new Cglib2AopProxy(advisedSupport).getProxy();
        System.out.println(cgLibProxy.register("userName"));;
    }
}
