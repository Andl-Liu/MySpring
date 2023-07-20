package cn.andl.springframework.beans.context.support;

import cn.andl.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.andl.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * XML应用上下文抽象类
 * 实现了父类加载Bean定义的方法
 * 留出了获取资源路径的抽象方法供子类实现
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {

    /**
     * 加载Bean定义
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // 获取 xmlBean定义读取器
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        // 获取资源路径
        String[] configLocations = getConfigLocations();
        // 读取Bean定义
        for (String configLocation : configLocations) {
            xmlBeanDefinitionReader.loadBeanDefinitions(configLocation);
        }
    }

    /**
     * 获取资源路径
     */
    protected abstract String[] getConfigLocations();
}
