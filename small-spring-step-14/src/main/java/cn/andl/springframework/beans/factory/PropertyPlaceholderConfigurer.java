package cn.andl.springframework.beans.factory;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.PropertyValue;
import cn.andl.springframework.beans.PropertyValues;
import cn.andl.springframework.beans.factory.config.BeanDefinition;
import cn.andl.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.andl.springframework.core.io.DefaultResourceLoader;
import cn.andl.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * 属性文件占位符配置器
 * 把属性定义中的占位符替换为具体的值
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            // 加载属性文件
            DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
            Resource resource = defaultResourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            // 遍历bean定义
            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);

                // 遍历属性值
                PropertyValues propertyValues = beanDefinition.getPropertyValues();
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    // 获取值
                    Object value = propertyValue.getValue();
                    // 如果值不是String，下一个
                    if (!(value instanceof String)) {
                        continue;
                    }
                    String strValue = (String) value;
                    StringBuilder buffer = new StringBuilder(strValue);
                    // 获取前缀和后缀的坐标
                    int startIdx = buffer.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
                    int stopIdx = buffer.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
                    // 当两者都找到，且位置合理时，进行处理
                    if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
                        // 获取键和值
                        String propKey = strValue.substring(startIdx + 2, stopIdx);
                        String propValue = properties.getProperty(propKey);
                        // 替换
                        buffer.replace(startIdx, stopIdx + 1, propValue);
                        // 将替换后的属性值添加到bean定义中
                        propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), buffer.toString()));
                    }
                }
            }
        } catch (IOException e) {
            throw new BeansException("Could not load properties", e);
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
