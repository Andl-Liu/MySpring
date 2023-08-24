package cn.andl.springframework.context.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.context.ApplicationEvent;
import cn.andl.springframework.context.ConfigurableApplicationContext;
import cn.andl.springframework.context.event.ApplicationEventMulticaster;
import cn.andl.springframework.context.event.ContextClosedEvent;
import cn.andl.springframework.context.event.ContextRefreshedEvent;
import cn.andl.springframework.context.event.SimpleApplicationEventMulticaster;
import cn.andl.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.andl.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.andl.springframework.beans.factory.config.BeanPostProcessor;
import cn.andl.springframework.core.convert.ConversionService;
import cn.andl.springframework.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 * 抽象应用上下文类
 * 提供刷新容器的模板方法
 *
 * 继承了默认的资源加载器，拥有了加载资源的功能
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";
    ApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void refresh() throws BeansException {
        // 1. 创建 BeanFactory ，并加载 BeanDefinition
        refreshBeanFactory();
        // 2. 获取 BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 3. 向beanFactory中添加 ApplicationContextAwareProcessor，以支持实现了ApplicationContextAware接口的类获取应用上下文
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        // 4. 在 Bean实例化之前，执行 BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);
        // 5. 先于其他，将 BeanPostProcessor 实例化
        registerBeanPostProcessors(beanFactory);
        // 6. 初始化事件发布者
        initApplicationEventMulticaster();
        // 7. 注册事件监听器
        registerListeners();
        // 8. 设置类型转换器，提前实例化bean
        finishBeanFactoryInitialization(beanFactory);
        // 9. 发布容器刷新完成事件
        finishRefresh();
    }

    /**
     * 设置类型转换器，并将单例bean提前实例化
     */
    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        // 设置类型转换器
        if (beanFactory.containsBean("conversionService")) {
            Object conversionService = beanFactory.getBean("conversionService");
            if (conversionService instanceof ConversionService) {
                beanFactory.setConversionService((ConversionService) conversionService);
            }
        }

        // 提前实例化bean对象
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
     * 初始化容器事件广播器
     */
    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
    }

    /**
     * 注册容器事件监听器
     */
    private void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener applicationListener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(applicationListener);
        }
    }

    /**
     * 结束容器刷新,发布结束容器刷新事件
     */
    void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    /**
     * 发布目标事件，并通知监听目标事件的监听器
     * @param event 目标事件
     */
    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    /**
     * 关闭应用上下文
     * 发布容器关闭事件
     * 执行销毁方法
     */
    @Override
    public void close() {
        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));
        // 销毁单例
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

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }
}
