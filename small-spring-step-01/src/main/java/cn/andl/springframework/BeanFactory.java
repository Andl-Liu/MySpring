package cn.andl.springframework;

import java.util.HashMap;
import java.util.Map;

/**
 * 简化Bean容器
 * @author Andl
 * @Created 2023/5/11 13:39
 */
public class BeanFactory {

    /**
     * 使用一个HashMap来存放BeanDefinition
     */
    Map<String, BeanDefinition> beanDefinitionMap = new HashMap<String, BeanDefinition>();

    /**
     * 注册BeanDefinition
     * <p>将BeanDefinition存放到bean容器中</p>
     * @param name 名字
     * @param beanDefinition 要被注册的bean
     */
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
    }

    /**
     * 通过注册时使用的名称来获取BeanDefinition
     * @param name 注册时使用的名称
     * @return 返回BeanDefinition定义的Bean对象
     */
    public Object getBean(String name) {
        return beanDefinitionMap.get(name).getBean();
    }

}
