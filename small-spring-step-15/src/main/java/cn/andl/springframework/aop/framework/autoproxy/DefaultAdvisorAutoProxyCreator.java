package cn.andl.springframework.aop.framework.autoproxy;

import cn.andl.springframework.aop.*;
import cn.andl.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.andl.springframework.aop.framework.ProxyFactory;
import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.PropertyValues;
import cn.andl.springframework.beans.factory.BeanFactory;
import cn.andl.springframework.beans.factory.BeanFactoryAware;
import cn.andl.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.andl.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    /**
     * 判断是否是基础类
     */
    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advisor.class.isAssignableFrom(beanClass)
                || Pointcut.class.isAssignableFrom(beanClass)
                || Advice.class.isAssignableFrom(beanClass);
    }

    /**
     * 在实例化之前，执行此方法
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        // 判断是不是基础类
        if (isInfrastructureClass(beanClass)) {
            return null;
        }

        // 获取所有的Advisor
        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        // 遍历Advisor
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            // 筛选符合条件的
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.matches(beanClass)) {
                continue;
            }

            // 将需要的信息包装进AdvisorSupport中

            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource = null;
            try {
                targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(false);

            // 创建并返回代理对象
            return new ProxyFactory(advisedSupport).getProxy();
        }

        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return null;
    }
}
