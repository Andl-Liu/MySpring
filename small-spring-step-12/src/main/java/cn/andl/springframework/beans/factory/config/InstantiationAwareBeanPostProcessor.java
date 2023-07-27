package cn.andl.springframework.beans.factory.config;

import cn.andl.springframework.beans.BeansException;

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

}
