package cn.andl.springframework.beans.factory;

import cn.andl.springframework.beans.BeansException;

import java.util.Map;

public interface ListableBeanFactory extends BeanFactory{

    /**
     * 按照类型返回Bean实例
     * @param type 目标类型
     * @return 符合条件的Bean
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    /**
     * 返回容器中所有注册了的Bean定义的名字
     * @return Bean定义的名字
     */
    String[] getBeanDefinitionNames();

}
