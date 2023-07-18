package cn.andl.springframework.beans.factory.config;

import cn.andl.springframework.beans.factory.HierarchicalBeanFactory;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 将Bean后置处理器添加进容器中
     * @param beanPostProcessor Bean后置处理器
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

}
