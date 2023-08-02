package cn.andl.springframework.beans.factory.config;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.ConfigurableListableBeanFactory;

/**
 * Bean工厂后置处理器接口
 * 提供 在所有Bean定义加载完成后、Bean实例化之前， 对 BeanDefinition 进行修改的机制
 */
public interface BeanFactoryPostProcessor {

    /**
     * 在所有Bean定义加载完成后、Bean实例化之前，对BeanDefinition进行修改
     * @param beanFactory bean工厂
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
