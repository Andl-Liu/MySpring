package cn.andl.springframework.test.common;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.factory.config.BeanPostProcessor;
import cn.andl.springframework.test.beans.UserService;

public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setLocation("赫格纳");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("userService".equals(beanName)) {
            System.out.println("初始化完成了哦~");
        }
        return bean;
    }
}
