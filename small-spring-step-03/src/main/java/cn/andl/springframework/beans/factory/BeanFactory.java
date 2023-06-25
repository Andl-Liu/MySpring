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
}
