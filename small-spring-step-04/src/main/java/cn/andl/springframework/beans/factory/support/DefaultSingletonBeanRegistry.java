package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例Bean注册的默认实现
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 储存单例Bean的容器
    Map<String, Object> singletonMap = new HashMap<>();

    /**
     * 获取单例Bean
     * @param name 名字
     * @return 单例Bean对象
     */
    @Override
    public Object getSingleton(String name) {
        return singletonMap.get(name);
    }

    /**
     * 向容器中添加单例Bean
     * @param name Bean的名字
     * @param singleton 单例Bean对象
     */
    protected void addSingleton(String name, Object singleton) {
        singletonMap.put(name, singleton);
    }
}
