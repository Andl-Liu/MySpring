package cn.andl.springframework.aop;

import java.lang.reflect.Method;

/**
 * 方法匹配器接口
 */
public interface MethodMatcher {

    boolean matches(Method method, Class<?> clazz);

}
