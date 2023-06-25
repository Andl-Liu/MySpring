package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean工厂的默认实现
 * 拥有BeanDefinition的容器
 * 负责AbstractBeanFactory中获取BeanDefinition的实现
 * 通过实现BeanDefinitionRegistry的接口，拥有了注册BeanDefinition的功能
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry{

    // BeanDefinition容器
    Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    /**
     * 注册Bean定义
     * @param beanName Bean的名字
     * @param beanDefinition Bean的定义
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    /**
     * 获取Bean定义
     * @param beanName Bean的名字
     * @return Bean定义
     * @throws BeansException 没有目标名字的Bean定义
     */
    @Override
    protected BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition == null) {
            throw new BeansException("no bean named '" + beanName + "' is defined");
        }
        return beanDefinition;
    }
}
