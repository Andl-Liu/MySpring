package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.DisposableBean;
import cn.andl.springframework.beans.factory.config.BeanDefinition;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Method;

/**
 * 即弃Bean适配器类
 * 用来给最终的销毁方法调用提供统一的方式
 */
public class DisposableBeanAdapter implements DisposableBean {

    private final String beanName;
    private final Object bean;
    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.beanName = beanName;
        this.bean = bean;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {
        // 1. 通过 DisposableBean 类实现的销毁方法
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

        // 2. 通过配置信息 destroy-method 实现的销毁方法; 同时要避免重复执行相同的销毁方法
        if (StrUtil.isNotEmpty(destroyMethodName)
                && !(bean instanceof DisposableBean && StrUtil.equals("destroy", destroyMethodName))) {
            Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
            if (null == destroyMethod) {
                throw new BeansException("Could not find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
            }
            destroyMethod.invoke(bean);
        }
    }
}
