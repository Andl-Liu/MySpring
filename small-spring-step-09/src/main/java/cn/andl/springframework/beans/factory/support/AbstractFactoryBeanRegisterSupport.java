package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象工厂Bean注册器支持类
 * 专门处理 FactoryBean 此类对象的注册服务
 */
public class AbstractFactoryBeanRegisterSupport extends DefaultSingletonBeanRegistry{
    // 单例工厂Bean生成的对象的缓存
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    /**
     * 从缓存中获取bean
     * @param beanName bean的名字
     * @return 目标bean
     */
    protected Object getCachedObjectForFactoryBean(String beanName) {
        return factoryBeanObjectCache.get(beanName);
    }

    /**
     * 从工厂Bean中获取Bean，同时判断是否为单例
     * @param factoryBean 工厂Bean
     * @param beanName bean的名字
     * @return 目标bean
     */
    protected Object getObjectFromFactoryBean(FactoryBean<?> factoryBean, String beanName) {
        if (factoryBean.isSingleton()) {
            Object bean = this.factoryBeanObjectCache.get(beanName);
            if (bean == null) {
                bean = doGetObjectFromFactoryBean(factoryBean, beanName);
                if (bean != null) {
                    this.factoryBeanObjectCache.put(beanName, bean);
                }
            }
            return bean;
        } else {
            return doGetObjectFromFactoryBean(factoryBean, beanName);
        }
    }

    /**
     * 从工厂Bean中获取Bean
     * @param factoryBean 工厂Bean
     * @param beanName bean的名字
     * @return 目标bean
     */
    private Object doGetObjectFromFactoryBean(FactoryBean<?> factoryBean, String beanName) {
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean threw exception on bean[" + beanName + "] creation.", e);
        }
    }

}
