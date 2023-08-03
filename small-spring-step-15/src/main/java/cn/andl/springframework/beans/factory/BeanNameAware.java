package cn.andl.springframework.beans.factory;

import cn.andl.springframework.beans.BeansException;

/**
 * 实现此接口，可以获取到BeanName
 */
public interface BeanNameAware extends Aware {

    void setBeanName(String beanName) throws BeansException;

}
