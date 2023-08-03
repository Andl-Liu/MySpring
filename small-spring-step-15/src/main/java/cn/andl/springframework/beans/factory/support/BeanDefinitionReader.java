package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.core.io.Resource;
import cn.andl.springframework.core.io.ResourceLoader;

/**
 * Bean定义读取器接口
 * @author Andl
 * @since 2023/7/11 9:48
 */
public interface BeanDefinitionReader {

    /**
     * 获取Bean定义注册器
     * @return Bean定义注册器
     */
    BeanDefinitionRegistry getRegistry();

    /**
     * 获取资源加载器
     * @return 资源加载器
     */
    ResourceLoader getResourceLoader();

    /**
     * 从资源加载Bean定义
     * @param resource 资源
     */
    void loadBeanDefinitions(Resource resource) throws BeansException;

    /**
     * 从多个资源加载Bean定义
     * @param resources 复数资源
     */
    void loadBeanDefinitions(Resource... resources) throws BeansException;

    /**
     * 从指定路径加载Bean定义
     * @param location 指定路径
     */
    void loadBeanDefinitions(String location) throws BeansException;
}
