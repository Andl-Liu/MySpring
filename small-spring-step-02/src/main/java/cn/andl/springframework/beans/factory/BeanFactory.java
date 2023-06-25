package cn.andl.springframework.beans.factory;

/**
 * Bean工厂接口
 */
public interface BeanFactory {

    /**
     * 通过名字获取Bean
     * @param name Bean的名字
     * @return Bean对象
     */
    Object getBean(String name);

}
