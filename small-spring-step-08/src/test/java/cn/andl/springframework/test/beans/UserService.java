package cn.andl.springframework.test.beans;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.context.ApplicationContext;
import cn.andl.springframework.beans.context.ApplicationContextAware;
import cn.andl.springframework.beans.factory.*;

public class UserService implements BeanFactoryAware, BeanClassLoaderAware, BeanNameAware, ApplicationContextAware {
    private String uId;
    private String company;
    private String location;
    private UserDao userDao;

    private ApplicationContext applicationContext;
    private ClassLoader classLoader;
    private BeanFactory beanFactory;
    private String beanName;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        System.out.println(this.applicationContext);
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) throws BeansException {
        this.classLoader = classLoader;
        System.out.println(this.classLoader);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        System.out.println(this.beanFactory);
    }

    @Override
    public void setBeanName(String beanName) throws BeansException {
        this.beanName = beanName;
        System.out.println(this.beanName);
    }

    public String queryUserInfo() {
        return userDao.queryUserName(uId)+", 公司："+company+", 地点"+location;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
