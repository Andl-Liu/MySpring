package cn.andl.springframework.aop;

import java.lang.reflect.Method;

/**
 * 前置方法通知接口
 */
public interface MethodBeforeAdvice extends BeforeAdvice {

    /**
     * 在目标方法调用前回调该方法
     * @param method 目标方法
     * @param args 目标方法的参数
     * @param target 目标对象
     */
    void before(Method method, Object[] args, Object target) throws Throwable;

}
