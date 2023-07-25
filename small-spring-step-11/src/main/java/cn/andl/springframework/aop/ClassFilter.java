package cn.andl.springframework.aop;

/**
 * 类过滤器接口
 */
public interface ClassFilter {

    boolean matches(Class<?> clazz);

}
