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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    // 存放未完成的代理引用
    private final Set<Object> earlyProxyReferences = Collections.synchronizedSet(new HashSet<>());

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
        // 如果未完成的代理集合中没有目标类，则进行包装，并返回代理类
        if (!earlyProxyReferences.contains(beanName)) {
            return wrapIfNecessary(bean, beanName);
        }
        return bean;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {
        // 判断是不是基础类
        if (isInfrastructureClass(bean.getClass())) {
            return null;
        }

        // 获取所有的Advisor
        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        // 遍历Advisor
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            // 筛选符合条件的
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.matches(bean.getClass())) {
                continue;
            }

            // 将需要的信息包装进AdvisorSupport中

            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource = null;
            try {
                // 将bean设置到target中，后续在调用自己的方法（不是拦截器的方法）的时候会使用bean
                targetSource = new TargetSource(bean);
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
        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return null;
    }
}
