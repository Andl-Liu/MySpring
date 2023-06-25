package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.factory.config.BeanDefinition;

/**
 * bean定义注册接口
 */
public interface BeanDefinitionRegistry {

    /**
     * 注册Bean定义
     * @param beanName Bean的名字
     * @param beanDefinition Bean的定义
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

}
