package cn.andl.springframework.aop.framework;

import cn.andl.springframework.aop.AdvisedSupport;

/**
 * 代理工厂
 * 解决Cglib和JDK的选择问题
 */
public class ProxyFactory {

    private final AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy() {
        return this.createAopProxy().getProxy();
    }

    private AopProxy createAopProxy() {
        if (this.advisedSupport.isProxyTargetClass()) {
            return new Cglib2AopProxy(advisedSupport);
        }
        return new JdkDynamicAopProxy(advisedSupport);
    }
}
