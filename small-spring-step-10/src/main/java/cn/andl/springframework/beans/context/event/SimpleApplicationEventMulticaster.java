package cn.andl.springframework.beans.context.event;

import cn.andl.springframework.beans.context.ApplicationEvent;
import cn.andl.springframework.beans.context.support.ApplicationListener;
import cn.andl.springframework.beans.factory.BeanFactory;

/**
 * 简单事件广播器
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void multicastEvent(ApplicationEvent applicationEvent) {
        for (final ApplicationListener applicationListener : getApplicationListeners(applicationEvent)) {
            applicationListener.onApplicationEvent(applicationEvent);
        }
    }
}
