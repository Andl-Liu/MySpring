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
     * 获取Bean的模板方法
     * @param name Bean的名字
     * @return Bean对象
     */
    @Override
    public Object getBean(String name) throws BeansException {
        Object bean = getSingleton(name);
        if(bean != null) {
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(name);
        return createBean(beanDefinition, name);
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
     * @return Bean对象
     */
    protected abstract Object createBean(BeanDefinition beanDefinition, String beanName) throws BeansException;

}
