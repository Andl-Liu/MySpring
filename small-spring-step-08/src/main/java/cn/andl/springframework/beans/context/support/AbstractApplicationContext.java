package cn.andl.springframework.beans.context.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.context.ConfigurableApplicationContext;
import cn.andl.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.andl.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.andl.springframework.beans.factory.config.BeanPostProcessor;
import cn.andl.springframework.core.io.DefaultResourceLoader;

import java.util.Map;

/**
 * 抽象应用上下文类
 * 提供刷新容器的模板方法
 *
 * 继承了默认的资源加载器，拥有了加载资源的功能
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    @Override
    public void refresh() throws BeansException {
        // 1. 创建 BeanFactory ，并加载 BeanDefinition
        refreshBeanFactory();
        // 2. 获取 BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 2-5. 向beanFactory中添加 ApplicationContextAwareProcessor，以支持实现了ApplicationContextAware接口的类获取应用上下文
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        // 3. 在 Bean实例化之前，执行 BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);
        // 4. 先于其他，将 BeanPostProcessor 实例化
        registerBeanPostProcessors(beanFactory);
        // 5. 将单例Bean提前实例化
        beanFactory.preInstantiateSingletons();
    }

    /**
     * 刷新Bean工厂
     */
    protected abstract void refreshBeanFactory() throws BeansException;

    /**
     * 获取Bean工厂
     * @return ConfigurableListableBeanFactory
     */
    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    /**
     * 调用所有Bean工厂后置处理器
     * @param beanFactory Bean工厂
     */
    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    /**
     * 将所有的Bean前置处理器注册到容器中
     * @param beanFactory Bean工厂
     */
    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    /**
     * 注册虚JVM的ShutdownHook
     * ShutdownHook（关闭时钩子）会在JVM关闭之前执行指定的线程
     * 此处指定线程执行close方法
     */
    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * 关闭应用上下文
     * 执行销毁方法
     */
    @Override
    public void close() {
        getBeanFactory().destroySingletons();
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> type) throws BeansException {
        return getBeanFactory().getBean(name, type);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }
}
