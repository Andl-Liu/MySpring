package cn.andl.springframework.beans.factory;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.config.AutowireCapableBeanFactory;
import cn.andl.springframework.beans.factory.config.BeanDefinition;
import cn.andl.springframework.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, ConfigurableBeanFactory, AutowireCapableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 提前初始化单例Bean
     */
    void preInstantiateSingletons() throws BeansException;
}
