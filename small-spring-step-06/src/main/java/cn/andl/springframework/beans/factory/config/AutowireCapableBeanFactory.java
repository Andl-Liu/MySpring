package cn.andl.springframework.beans.factory.config;

import cn.andl.springframework.beans.factory.BeanFactory;

/**
 * 提供一些自动装配相关的方法 的接口
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 执行所有 BeanPostProcessor 接口实现类的 postProcessBeforeInitialization 方法
     * @param existingBean 需要操作的目标bean
     * @param beanName bean的名字
     * @return 处理后的bean
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName);

    /**
     * 执行所有 BeanPostProcessor 接口实现类的 postProcessAfterInitialization 方法
     * @param existingBean 需要操作的目标bean
     * @param beanName bean的名字
     * @return 处理后的bean
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName);

}
