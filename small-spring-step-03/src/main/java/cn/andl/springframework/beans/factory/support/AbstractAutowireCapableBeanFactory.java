package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 实例化Bean类
 * 负责AbstractBeanFactory类中的实例化对象的功能
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    // 实例化策略
    InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    /**
     * 创建bean对象
     * @param beanDefinition Bean定义
     * @param beanName Bean的名字
     * @return bean对象
     * @throws BeansException 实例化bean失败
     */
    @Override
    protected Object createBean(BeanDefinition beanDefinition, String beanName, Object[] args) throws BeansException {
        Object bean = null;
        try {
            bean = instantiateBean(beanDefinition, beanName, args);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }
        // 将bean对象添加到单例bean容器中
        addSingleton(beanName, bean);
        return bean;
    }

    /**
     * 实例化bean对象
     * @param beanDefinition bean定义
     * @param beanName bean名字
     * @param args 参数列表
     * @return bean对象
     */
    private Object instantiateBean(BeanDefinition beanDefinition, String beanName, Object[] args) {
        Constructor constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        // 循环筛选参数列表符合要求的构造器
        for(Constructor constructor : declaredConstructors) {
            if (null != args && constructor.getParameterTypes().length == args.length) {
                boolean flag = true;
                Class[] parameterTypes = constructor.getParameterTypes();
                for(int i = 0;i < args.length;i++) {
                    if(!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
                        flag = false;
                        break;
                    }
                }
                if(flag) {
                    constructorToUse = constructor;
                    break;
                }
            }
        }
        return instantiationStrategy.instantiate(beanDefinition, beanName, constructorToUse, args);
    }
}
