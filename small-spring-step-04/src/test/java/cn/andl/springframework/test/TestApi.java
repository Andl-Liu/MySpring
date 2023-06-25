package cn.andl.springframework.test;

import cn.andl.springframework.beans.PropertyValue;
import cn.andl.springframework.beans.PropertyValues;
import cn.andl.springframework.beans.factory.config.BeanDefinition;
import cn.andl.springframework.beans.factory.config.BeanReference;
import cn.andl.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.andl.springframework.test.beans.UserDao;
import cn.andl.springframework.test.beans.UserService;
import org.junit.Test;

public class TestApi {

    @Test
    public void test_BeanFactory() {
        // 1. 初始化bean工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2. 注册 UserDao
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));
        // 3. 为 UserService 设置属性
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uid", "1001"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));
        // 4. 注册 UserService
        beanFactory.registerBeanDefinition("userService", new BeanDefinition(UserService.class, propertyValues));
        // 5. 获取 UserService
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryInfo();
    }

}
