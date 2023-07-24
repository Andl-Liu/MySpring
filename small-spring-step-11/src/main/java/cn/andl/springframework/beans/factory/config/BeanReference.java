package cn.andl.springframework.beans.factory.config;

/**
 * bean引用
 * 用于在依赖注入时，作为值存贮在PropertyValue中
 */
public class BeanReference {

    final private String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
