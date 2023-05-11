package cn.andl.springframework.test;

import cn.andl.springframework.BeanDefinition;
import cn.andl.springframework.BeanFactory;
import cn.andl.springframework.test.bean.UserService;
import org.junit.Test;

/**
 * 接口测试类
 * @author Andl
 * @Created 2023/5/11 17:14
 */
public class ApiTest {

    @Test
    public void testBeanFactory() {
        // 1.初始化容器
        BeanFactory beanFactory = new BeanFactory();
        // 2.注册bean
        beanFactory.registerBeanDefinition("userService", new BeanDefinition(new UserService()));
        // 3.获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.query();
    }

}
