package cn.andl.springframework.test;

import cn.andl.springframework.beans.PropertyValue;
import cn.andl.springframework.beans.PropertyValues;
import cn.andl.springframework.beans.context.support.ClassPathXmlApplicationContext;
import cn.andl.springframework.beans.factory.config.BeanDefinition;
import cn.andl.springframework.beans.factory.config.BeanReference;
import cn.andl.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.andl.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import cn.andl.springframework.core.io.DefaultResourceLoader;
import cn.andl.springframework.core.io.Resource;
import cn.andl.springframework.test.beans.UserDao;
import cn.andl.springframework.test.beans.UserService;
import cn.andl.springframework.test.common.MyBeanFactoryPostProcessor;
import cn.andl.springframework.test.common.MyBeanPostProcessor;
import cn.hutool.core.io.IoUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class TestApi {

    @Test
    public void test_BeanFactoryPostProcessorAndBeanPostProcessor() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, defaultResourceLoader);
        xmlBeanDefinitionReader.loadBeanDefinitions("classpath:spring1.xml");
        // 调用bean工厂后置处理器
        MyBeanFactoryPostProcessor myBeanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
        myBeanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        // 添加Bean后置处理器
        MyBeanPostProcessor myBeanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(myBeanPostProcessor);
        UserService userService = (UserService)beanFactory.getBean("userService");
        System.out.println(userService.queryUserInfo());
    }


    @Test
    public void test_xmlApplicationContext() {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:spring2.xml");
        UserService userService = (UserService) classPathXmlApplicationContext.getBean("userService");
        System.out.println(userService.queryUserInfo());
    }

}
