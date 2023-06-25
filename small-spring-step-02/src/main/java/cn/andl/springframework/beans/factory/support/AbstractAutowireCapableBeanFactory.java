package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.config.BeanDefinition;

/**
 * 实例化Bean类
 * 负责AbstractBeanFactory类中的实例化对象的功能
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    /**
     * 实例化bean对象
     * @param beanDefinition Bean定义
     * @param beanName Bean的名字
     * @return bean对象
     * @throws BeansException 实例化bean失败
     */
    @Override
    protected Object createBean(BeanDefinition beanDefinition, String beanName) throws BeansException {
        Object bean = null;
        try {
            bean = beanDefinition.getBeanClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeansException("Instantiation of bean failed", e);
        }
        // 将bean对象添加到单例bean容器中
        addSingleton(beanName, bean);
        return bean;
    }
}
