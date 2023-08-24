package cn.andl.springframework.beans.factory;

import cn.andl.springframework.beans.BeansException;

/**
 * Bean工厂接口
 */
public interface BeanFactory {

    /**
     * 通过名字获取Bean
     * @param name Bean的名字
     * @return Bean对象
     */
    Object getBean(String name) throws BeansException;

    /**
     * 通过名字和参数列表获取Bean
     * @param name Bean的名字
     * @param args 参数列表
     * @return Bean对象
     */
    Object getBean(String name, Object... args) throws BeansException;

    /**
     * 根据名字获取Bean，并转换为指定的类型
     * @param name Bean的名字
     * @param type 类
     * @return Bean对象
     */
    <T> T getBean(String name, Class<T> type) throws BeansException;

    /**
     * 根据类型获取bean，并转换为指定的类型
     * @param requiredType 需要的类型
     * @return bean
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;

    /**
     * 判断容器中是否有目标bean
     * @param name 目标bean的名字
     * @return 是否存在
     */
    boolean containsBean(String name);
}
