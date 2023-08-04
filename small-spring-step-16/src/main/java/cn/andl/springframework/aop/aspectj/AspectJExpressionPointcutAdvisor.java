package cn.andl.springframework.aop.aspectj;

import cn.andl.springframework.aop.Pointcut;
import cn.andl.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

/**
 * 一个包装类
 * 实现了PointcutAdvisor接口
 * 将 切点、通知方法和切点表达式 包装在一起
 * 可通过该类，在xml的配置中定义一个PointcutAdvisor的切面拦截器
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    // 切点
    private AspectJExpressionPointcut pointcut;
    // 通知
    private Advice advice;
    // 表达式
    private String expression;

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public Pointcut getPointcut() {
        if (null == this.pointcut) {
            this.pointcut = new AspectJExpressionPointcut(expression);
        }
        return this.pointcut;
    }
}
