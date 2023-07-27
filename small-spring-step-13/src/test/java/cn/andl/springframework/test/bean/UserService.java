package cn.andl.springframework.test.bean;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.context.ApplicationContext;
import cn.andl.springframework.beans.context.ApplicationContextAware;
import cn.andl.springframework.beans.factory.BeanClassLoaderAware;
import cn.andl.springframework.beans.factory.BeanFactory;
import cn.andl.springframework.beans.factory.BeanFactoryAware;
import cn.andl.springframework.beans.factory.BeanNameAware;
import cn.andl.springframework.stereotype.Component;

import java.util.Random;

@Component("userService")
public class UserService implements IUserService {

    private String token;

    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "小傅哥，100001，深圳";
    }

    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + userName + " success！";
    }

    @Override
    public String toString() {
        return "UserService#token = { " + token + " }";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}