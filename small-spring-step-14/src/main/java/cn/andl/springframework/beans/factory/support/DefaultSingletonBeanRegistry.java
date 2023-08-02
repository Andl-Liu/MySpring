package cn.andl.springframework.beans.factory.support;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.DisposableBean;
import cn.andl.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 单例Bean注册的默认实现
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 储存单例Bean的容器
    Map<String, Object> singletonMap = new HashMap<>();

    // 储存即弃Bean，用来进行销毁操作
    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

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
    public void addSingleton(String name, Object singleton) {
        singletonMap.put(name, singleton);
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
