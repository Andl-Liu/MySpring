package cn.andl.springframework.aop.framework;

import cn.andl.springframework.aop.AdvisedSupport;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 用JDK的方式实现代理
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advisedSupport;

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), advisedSupport.getTargetSource().getTargetClass(), this);
    }

    /**
     * 如果匹配，则调用代理方法；如果不匹配，则调用原方法
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 调用代理方法
        if (advisedSupport.getMethodMatcher().matches(method, advisedSupport.getTargetSource().getTarget().getClass())) {
            MethodInterceptor methodInterceptor = advisedSupport.getMethodInterceptor();
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(advisedSupport.getTargetSource().getTarget(), method, args));
        }
        // 调用原方法
        return method.invoke(advisedSupport.getTargetSource().getTarget(), args);
    }
}
