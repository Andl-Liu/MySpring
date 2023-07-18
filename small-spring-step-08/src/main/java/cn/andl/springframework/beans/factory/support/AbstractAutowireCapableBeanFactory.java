package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.PropertyValue;
import cn.andl.springframework.beans.PropertyValues;
import cn.andl.springframework.beans.factory.DisposableBean;
import cn.andl.springframework.beans.factory.InitializingBean;
import cn.andl.springframework.beans.factory.config.AutowireCapableBeanFactory;
import cn.andl.springframework.beans.factory.config.BeanDefinition;
import cn.andl.springframework.beans.factory.config.BeanPostProcessor;
import cn.andl.springframework.beans.factory.config.BeanReference;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 实例化Bean类
 * 负责AbstractBeanFactory类中的实例化对象的功能
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

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
            // 创建bean实例
            bean = createBeanInstance(beanDefinition, beanName, args);
            // 为bean注入属性和依赖对象
            applyPropertyValues(beanName, bean, beanDefinition);
            // 调用Bean的初始化方法和后置Bean处理器
            initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        // 注册Bean的销毁方法
        registerDisposableBeanIfNecessary(bean, beanName, beanDefinition);

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
    private Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
        Constructor<?> constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        // 循环筛选参数列表符合要求的构造器
        for(Constructor<?> constructor : declaredConstructors) {
            // 参数列表非空 && 参数列表长度相同的时候继续
            if (null != args && constructor.getParameterTypes().length == args.length) {
                // 所有参数的参数类型相符合的时候继续
                boolean flag = true;
                Class<?>[] parameterTypes = constructor.getParameterTypes();
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

    /**
     * 为bean注入属性和依赖对象
     * @param beanName bean名字
     * @param bean bean对象
     * @param beanDefinition bean定义
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for(PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();

                if(value instanceof BeanReference) {
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                // 使用hutool工具类为属性赋值
                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (BeansException e) {
            throw new BeansException("Error setting property values: " + beanName, e);
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    /**
     * 调用Bean的初始化方法和后置Bean处理器
     * @param beanName bean的名字
     * @param bean 目标bean
     * @param beanDefinition bean定义
     */
    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 1. 调用 BeanPostProcessor 的 postProcessBeforeInitialization() 方法
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // 2. 调用初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Invocation of init method of bean [" + beanName + "] failed", e);
        }

        // 3. 调用 BeanPostProcessor 的 postProcessAfterInitialization() 方法
        return applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }

    /**
     * 调用bean的初始化方法（不同于构造方法）
     * @param beanName bean的名字
     * @param bean 目标Bean
     * @param beanDefinition bean定义
     */
    private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        // 如果 目标bean 实现了 InitializingBean接口, 调用初始化方法
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }

        // 当属性 initMethodName 不为空时, 获取初始化方法并调用, 同时避免重复执行
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName)
                && !(bean instanceof InitializingBean && StrUtil.equals("afterPropertiesSet", initMethodName))) {
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            if (null == initMethod) {
                throw new BeansException("Could not find an init method named '" + initMethodName + "' on bean with name '" + beanName + "'");
            }
            initMethod.invoke(bean);
        }

    }

    /**
     * 执行所有 BeanPostProcessor 接口实现类的 postProcessBeforeInitialization 方法
     * @param existingBean 需要操作的目标bean
     * @param beanName bean的名字
     */
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 执行所有 BeanPostProcessor 接口实现类的 postProcessAfterInitialization 方法
     * @param existingBean 需要操作的目标bean
     * @param beanName bean的名字
     */
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 注册即弃Bean
     * @param bean
     * @param beanName
     * @param beanDefinition
     */
    protected void registerDisposableBeanIfNecessary(Object bean, String beanName, BeanDefinition beanDefinition) {
        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }
}
