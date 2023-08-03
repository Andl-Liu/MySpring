package cn.andl.springframework.context.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.context.ApplicationContext;
import cn.andl.springframework.context.ApplicationContextAware;
import cn.andl.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 应用上下文感知类-Bean后置处理器
 * 在Bean初始化之前，给实现了ApplicationContextAware接口的类设置应用上下文
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
