package cn.andl.springframework.beans.factory.config;

import cn.andl.springframework.beans.factory.HierarchicalBeanFactory;
import cn.andl.springframework.utils.StringValueResolver;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 将Bean后置处理器添加进容器中
     * @param beanPostProcessor Bean后置处理器
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    /**
     * 向容器中添加一个字符串解析器
     * @param valueResolver 字符串解析器
     */
    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    /**
     * 解析字符串的内嵌值
     * @param value 字符串
     * @return 解析后的字符串
     */
    String resolveEmbeddedValue(String value);
}
