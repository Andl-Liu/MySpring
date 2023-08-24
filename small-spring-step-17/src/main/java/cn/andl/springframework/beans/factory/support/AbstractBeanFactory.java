package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.BeanFactory;
import cn.andl.springframework.beans.factory.FactoryBean;
import cn.andl.springframework.beans.factory.config.BeanDefinition;
import cn.andl.springframework.beans.factory.config.BeanPostProcessor;
import cn.andl.springframework.beans.factory.config.ConfigurableBeanFactory;
import cn.andl.springframework.core.convert.ConversionService;
import cn.andl.springframework.utils.ClassUtils;
import cn.andl.springframework.utils.StringValueResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象Bean工厂
 * 通过继承DefaultSingletonBeanRegistry，拥有了注册和获取单例Bean的功能
 * 通过实现BeanFactory接口，拥有了获取Bean的功能
 * 使用了模板方法设计模式，
 */
public abstract class AbstractBeanFactory extends AbstractFactoryBeanRegisterSupport implements ConfigurableBeanFactory {

    // 后置Bean处理器列表
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    // 内嵌字符串处理器
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    private ConversionService conversionService;

    /**
     * 通过名字获取bean
     * @param name Bean的名字
     * @return bean对象
     */
    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    /**
     * 通过名字和参数列表获取bean
     * @param name Bean的名字
     * @param args 参数列表
     * @return bean对象
     */
    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    /**
     * 根据名字获取Bean，并转化为指定的类型
     * @param name Bean的名字
     * @param type 类

     */
    @Override
    public <T> T getBean(String name, Class<T> type) throws BeansException {
        return (T) getBean(name);
    }

    /**
     * 获取Bean的模板方法
     * @param beanName bean的名字
     * @param args 参数列表
     * @return bean对象
     */
    protected <T> T doGetBean(final String beanName, final Object[] args){
        Object bean = getSingleton(beanName);
        if(bean != null) {
            return (T) getObjectForBeanInstance(bean, beanName);
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        bean = createBean(beanDefinition, beanName, args);
        return (T) getObjectForBeanInstance(bean, beanName);
    }

    /**
     * 获取Bean定义的抽象方法
     * @param beanName Bean的名字
     * @return Bean定义
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }

    protected abstract boolean containsBeanDefinition(String beanName);

    /**
     * 实例化Bean对象
     * @param beanDefinition Bean定义
     * @param beanName Bean的名字
     * @param args 参数列表
     * @return Bean对象
     */
    protected abstract Object createBean(BeanDefinition beanDefinition, String beanName, Object[] args) throws BeansException;

    /**
     * 从bean实例中获取目标对象（如果bean实例是工厂bean，获取工厂bean生产的对象）
     * @param beanInstance bean实例
     * @param beanName bean的名字
     * @return 目标bean
     */
    private Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        // 如果不是工厂bean，直接返回自己
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }
        // 如果是工厂bean，获取并返回工厂bean生产的对象
        Object object = getCachedObjectForFactoryBean(beanName);
        if (object == null) {
            FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
            object = getObjectFromFactoryBean(factoryBean, beanName);
        }
        return object;
    }

    /**
     * 将bean后置处理器添加到容器中
     * @param beanPostProcessor Bean后置处理器
     */
    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // 防止重复，先删除一次
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver stringValueResolver : embeddedValueResolvers) {
            result = stringValueResolver.resolveStringValue(result);
        }
        return result;
    }

    @Override
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ConversionService getConversionService() {
        return this.conversionService;
    }
}
