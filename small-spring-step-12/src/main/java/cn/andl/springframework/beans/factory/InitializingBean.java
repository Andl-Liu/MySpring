package cn.andl.springframework.beans.factory;

import cn.andl.springframework.beans.BeansException;

/**
 * 提供了初始化操作的接口
 */
public interface InitializingBean {

    /**
     * 属性注入完成后调用
     */
    void afterPropertiesSet() throws Exception;

}
