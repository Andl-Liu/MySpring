package cn.andl.springframework.beans.factory.config;

import cn.andl.springframework.beans.BeansException;

/**
 * Bean后置处理器接口
 * 提供 在Bean初始化方法执行前后 对Bean进行拓展的机制
 */
public interface BeanPostProcessor {

    /**
     * 在Bean初始化方法执行之前拓展Bean
     * @param bean bean
     * @param beanName bean名字
     * @return 处理后的bean
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * 在Bean初始化方法执行之后拓展Bean
     * @param bean bean
     * @param beanName bean名字
     * @return 处理后的bean
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
