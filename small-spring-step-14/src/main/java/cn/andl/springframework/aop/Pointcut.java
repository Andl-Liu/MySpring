package cn.andl.springframework.aop;

/**
 * 切点接口
 */
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();

}
