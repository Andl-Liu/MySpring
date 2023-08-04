package cn.andl.springframework.aop;

/**
 * 获取 pointcut 和 advice 的接口
 */
public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();

}
