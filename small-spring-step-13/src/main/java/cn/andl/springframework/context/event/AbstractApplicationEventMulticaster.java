package cn.andl.springframework.context.event;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.context.ApplicationEvent;
import cn.andl.springframework.context.support.ApplicationListener;
import cn.andl.springframework.beans.factory.BeanFactory;
import cn.andl.springframework.beans.factory.BeanFactoryAware;
import cn.andl.springframework.utils.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> applicationListener) {
        this.applicationListeners.add((ApplicationListener<ApplicationEvent>) applicationListener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> applicationListener) {
        this.applicationListeners.remove(applicationListener);
    }

    /**
     * 获取监听目标事件的监听器
     * @param event 目标事件
     * @return 筛选后的监听器
     */
    protected Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener> allListeners = new LinkedList<>();
        // 逐个筛选
        for (ApplicationListener<ApplicationEvent> applicationListener : applicationListeners) {
            if (supportsEvent(applicationListener, event)) {
                allListeners.add(applicationListener);
            }
        }
        return allListeners;
    }

    /**
     * 判断目标监听器是否监听目标事件
     * @param applicationListener 目标监听器
     * @param event 目标事件
     * @return 是否监听
     */
    protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
        Class<? extends ApplicationListener> listenerClass = applicationListener.getClass();

        // 按照不同的实例化策略，获取目标 class
        Class<?> targetClass = ClassUtils.isCglibProxyClass(listenerClass) ? listenerClass.getSuperclass() : listenerClass;

        // 获取监听器中，事件的class
        // 先获取监听器实现的接口的泛型，也就是 ApplicationListener 的泛型
        Type genericInterface = targetClass.getGenericInterfaces()[0];
        // 再获取代表ApplicationEvent的泛型
        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        // 通过代表ApplicationEvent的泛型获取Event的具体类名
        String className = actualTypeArgument.getTypeName();
        // 获取Event的具体类
        Class<?> eventClassName;
        try {
            eventClassName = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException("Wrong event class name: " + className, e);
        }
        // 判断目标类是否是监听器监听的类或其子类
        return eventClassName.isAssignableFrom(event.getClass());
    }

}
