package cn.andl.springframework.aop;

import org.aopalliance.aop.Advice;

/**
 * 用于获取Advice的接口
 */
public interface Advisor {

    Advice getAdvice();

}
