package cn.andl.springframework.beans.factory;

import cn.andl.springframework.beans.BeansException;

/**
 * 实现此接口，可以获取到所属的BeanFactory
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;

}
