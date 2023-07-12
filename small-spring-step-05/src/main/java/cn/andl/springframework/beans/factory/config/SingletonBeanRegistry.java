package cn.andl.springframework.beans.factory.config;

/**
 * 单例Bean注册接口
 */
public interface SingletonBeanRegistry {

    /**
     * 通过名字获取单例Bean
     * @param name 名字
     * @return 单例Bean对象
     */
    Object getSingleton(String name);

}
