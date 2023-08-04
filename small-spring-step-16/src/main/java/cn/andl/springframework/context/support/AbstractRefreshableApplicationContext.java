package cn.andl.springframework.context.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.andl.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 可刷新的应用上下文抽象类
 * 实现了父类刷新模板方法中的刷新BeanFactory和获取BeanFactory的方法
 * 留出了加载Bean定义的抽象方法供子类进行实现
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    DefaultListableBeanFactory beanFactory;

    /**
     * 刷新Bean工厂
     */
    @Override
    protected void refreshBeanFactory() throws BeansException {
        // 新建Bean工厂
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        // 加载Bean定义
        loadBeanDefinitions(beanFactory);
        // 赋值
        this.beanFactory = beanFactory;
    }

    /**
     * 新建Bean工厂
     */
    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    /**
     * 加载Bean定义
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    @Override
    protected ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
