package cn.andl.springframework.beans.context;

import cn.andl.springframework.beans.factory.ListableBeanFactory;

/**
 * 应用上下文接口
 */
public interface ApplicationContext extends ListableBeanFactory, ApplicationEventPublisher {
}
