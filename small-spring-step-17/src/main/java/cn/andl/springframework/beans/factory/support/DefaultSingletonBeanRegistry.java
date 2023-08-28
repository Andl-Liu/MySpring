package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.DisposableBean;
import cn.andl.springframework.beans.factory.ObjectFactory;
import cn.andl.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例Bean注册的默认实现
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 一级缓存
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
    // 二级缓存
    private final Map<String, Object> earlySingletonObjects = new HashMap<>();
    // 三级缓存
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();

    // 储存即弃Bean，用来进行销毁操作
    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    /**
     * 获取单例Bean
     * @param beanName 名字
     * @return 单例Bean对象
     */
    @Override
    public Object getSingleton(String beanName) {
        // 从一级缓存中获取对象
        Object singletonObject = singletonObjects.get(beanName);
        if (singletonObject != null) {
            return singletonObject;
        }

        // 如果一级缓存中没有，从二级缓存中获取
        singletonObject = earlySingletonObjects.get(beanName);
        if (singletonObject != null) {
            return singletonObject;
        }

        // 如果二级缓存中没有，从三级缓存中获取工厂
        ObjectFactory<?> objectFactory = singletonFactories.get(beanName);
        if (objectFactory != null) {
            // 如果能获取到工厂，从工厂中获取bean
            singletonObject = objectFactory.getObject();
            // 将bean添加到二级缓存中
            earlySingletonObjects.put(beanName, singletonObject);
            // 把对应的工厂从三级缓存中删除
            singletonFactories.remove(beanName);
        }

        return singletonObject;
    }

    /**
     * 向容器中添加单例Bean
     * @param name Bean的名字
     * @param singleton 单例Bean对象
     */
    @Override
    public void registerSingleton(String name, Object singleton) {
        singletonObjects.put(name, singleton);
        earlySingletonObjects.remove(name);
        singletonFactories.remove(name);
    }

    /**
     * 向三级缓存中添加工厂
     * @param beanName bean的名字
     * @param singletonFactory 工厂
     */
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        if (!singletonFactories.containsKey(beanName)) {
            singletonFactories.put(beanName, singletonFactory);
            earlySingletonObjects.remove(beanName);
        }
    }

    /**
     * 向即弃Bean容器中存放即弃Bean
     * @param beanName bean名字
     * @param disposableBean 即弃Bean
     */
    public void registerDisposableBean(String beanName, DisposableBean disposableBean) {
        disposableBeans.put(beanName, disposableBean);
    }

    /**
     * 销毁单例Bean
     */
    @Override
    public void destroySingletons() {
        Set<String> keySet = this.disposableBeans.keySet();
        String[] disposableBeanNames = keySet.toArray(new String[0]);

        for (int i = disposableBeanNames.length - 1;i >= 0;i--) {
            String beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy on bean named '" + beanName + "' threw an exception", e);
            }
        }
    }
}
