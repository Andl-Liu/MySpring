package cn.andl.springframework.beans.factory.config;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.PropertyValues;

/**
 * adds a before-instantiation callback
 * and a callback after instantiation but before explicit properties are set
 * or autowiring occurs
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{

    /**
     * 在实例化之前，执行此方法
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    /**
     * 在实例化之后，执行此方法
     */
    default boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    /**
     * 后置处理PropertyValues
     * @param pvs PropertyValues
     * @param bean 目标bean
     * @param beanName bean的名字
     * @return 处理后的PropertyValues
     */
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;

    /**
     * 获取未完成bean的引用
     */
    default Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
