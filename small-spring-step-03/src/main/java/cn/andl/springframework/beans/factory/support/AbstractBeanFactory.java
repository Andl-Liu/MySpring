package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.BeanFactory;
import cn.andl.springframework.beans.factory.config.BeanDefinition;

/**
 * 抽象Bean工厂
 * 通过继承DefaultSingletonBeanRegistry，拥有了注册和获取单例Bean的功能
 * 通过实现BeanFactory接口，拥有了获取Bean的功能
 * 使用了模板方法设计模式，
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    /**
     * 通过名字获取bean
     * @param name Bean的名字
     * @return bean对象
     */
    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    /**
     * 通过名字和参数列表获取bean
     * @param name Bean的名字
     * @param args 参数列表
     * @return bean对象
     */
    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    /**
     * 获取Bean的模板方法
     * @param beanName bean的名字
     * @param args 参数列表
     * @return bean对象
     */
    protected Object doGetBean(final String beanName, final Object[] args){
        Object bean = getSingleton(beanName);
        if(bean != null) {
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        return createBean(beanDefinition, beanName, args);
    }

    /**
     * 获取Bean定义的抽象方法
     * @param beanName Bean的名字
     * @return Bean定义
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 实例化Bean对象
     * @param beanDefinition Bean定义
     * @param beanName Bean的名字
     * @param args 参数列表
     * @return Bean对象
     */
    protected abstract Object createBean(BeanDefinition beanDefinition, String beanName, Object[] args) throws BeansException;

}
