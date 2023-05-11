package cn.andl.springframework;

/**
 * 简化Bean定义类
 * @author Andl
 * @Created 2023/5/11 13:37
 */
public class BeanDefinition {

    /**
     * 要被定义的bean
     */
    private Object bean;

    /**
     * 构造方法
     * @param bean 要被定义的bean
     */
    public BeanDefinition(Object bean) {
        this.bean = bean;
    }

    /**
     * 获取BeanDefinition定义的bean
     * @return BeanDefinition定义的bean
     */
    public Object getBean() {
        return bean;
    }

}
