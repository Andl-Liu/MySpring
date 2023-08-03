package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.PropertyValue;
import cn.andl.springframework.beans.PropertyValues;
import cn.andl.springframework.beans.factory.*;
import cn.andl.springframework.beans.factory.config.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

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
            // 做实例化前的处理，生成代理对象
            bean = resolveBeforeInstantiation(beanName, beanDefinition);
            if (bean != null) {
                return bean;
            }

            // 创建bean实例
            bean = createBeanInstance(beanDefinition, beanName, args);
            // 在设置bean的属性前，允许修改bean的属性值
            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
            // 为bean注入属性和依赖对象
            applyPropertyValues(beanName, bean, beanDefinition);
            // 调用Bean的初始化方法和后置Bean处理器
            initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        // 注册Bean的销毁方法
        registerDisposableBeanIfNecessary(bean, beanName, beanDefinition);

        // 如果对象是单例，将bean对象添加到单例bean容器中
        if (beanDefinition.isSingleton()) {
            addSingleton(beanName, bean);
        }
        return bean;
    }

    /**
     * 进行实例化之前的处理
     * 用于生成代理对象
     */
    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        // 获取代理对象
        Object bean = applyBeanPostProcessorBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if (bean != null) {
            // 应用初始化后的后置bean处理器
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }

    /**
     * 在实例化之前应用后置Bean处理器
     * 用于生成代理对象
     */
    private Object applyBeanPostProcessorBeforeInstantiation(Class<?> beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            // 调用实例化前的后置Bean处理器
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
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
     * 在为bean注入属性和依赖对象之前，应用beanPostProcessor，对属性值进行修改
     */
    private void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        List<BeanPostProcessor> beanPostProcessors = getBeanPostProcessors();
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            if (!(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor)) {
                continue;
            }
            // 获取处理后的PropertyValues
            PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
            if (pvs == null) {
                continue;
            }
            // 将处理后的PropertyValue循环添加到bean定义中
            for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
        }
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

        // 调用感知接口的方法
        if (bean instanceof Aware) {
            // 感知 BeanFactory
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            // 感知 BeanClassLoader
            if (bean instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }
            // 感知 BeanName
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }


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
     * @param bean 目标bean
     * @param beanName bean的名字
     * @param beanDefinition bean定义
     */
    protected void registerDisposableBeanIfNecessary(Object bean, String beanName, BeanDefinition beanDefinition) {
        // 非 Singleton 的 bean 不执行销毁方法
        if (!beanDefinition.isSingleton()) {
            return;
        }

        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }
}
