package cn.andl.springframework.beans.factory;

import cn.andl.springframework.beans.BeansException;

/**
 * 用来当放在三级缓存里的工厂
 */
public interface ObjectFactory<T> {

    T getObject() throws BeansException;

}
