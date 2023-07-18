package cn.andl.springframework.beans.context;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.Aware;

/**
 * 实现此接口，可以获取所属的应用上下文
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
