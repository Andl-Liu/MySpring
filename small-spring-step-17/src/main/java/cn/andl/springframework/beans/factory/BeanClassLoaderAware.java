package cn.andl.springframework.beans.factory;

import cn.andl.springframework.beans.BeansException;

/**
 * 实现此接口，可以获取所属的ClassLoader
 */
public interface BeanClassLoaderAware extends Aware{

    void setBeanClassLoader(ClassLoader classLoader) throws BeansException;

}
