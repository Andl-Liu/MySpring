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

    /**
     * 获取Bean定义
     * @param beanName Bean的名字
     * @return Bean定义
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 判断是否包含目标Bean
     * @param beanName bean的名字
     * @return 是否包含
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 获取注册表中所有bean的名字
     * @return 注册表中所有bean的名字
     */
    String[] getBeanDefinitionNames();

}
