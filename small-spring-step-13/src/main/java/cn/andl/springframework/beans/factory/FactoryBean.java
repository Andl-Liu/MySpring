package cn.andl.springframework.beans.factory;

import cn.andl.springframework.beans.BeansException;

/**
 * 工厂Bean接口
 * 通过实现这个接口 可以向容器提供一个使用者自定义的复杂Bean对象
 */
public interface FactoryBean<T> {

    T getObject() throws BeansException;

    Class<?> getObjectType();

    boolean isSingleton();

}
