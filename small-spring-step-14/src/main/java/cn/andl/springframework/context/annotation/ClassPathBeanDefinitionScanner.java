package cn.andl.springframework.context.annotation;

import cn.andl.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import cn.andl.springframework.beans.factory.config.BeanDefinition;
import cn.andl.springframework.beans.factory.support.BeanDefinitionRegistry;
import cn.andl.springframework.stereotype.Component;
import cn.hutool.core.util.StrUtil;

import java.util.Set;

/**
 * 类路径bean定义扫描器
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * 扫描目标包，将包中的组件注册进容器中
     */
    public void doScan(String... basePackages) {
        for(String basePackage : basePackages) {
            // 获取候选组件
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            // 遍历候选组件
            for (BeanDefinition beanDefinition : candidates) {
                // 解析候选组件的作用域
                String scope = resolveBeanScope(beanDefinition);
                if (StrUtil.isNotEmpty(scope)) {
                    beanDefinition.setScope(scope);
                }
                // 将组件注册进容器中
                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }

        // 向容器中注入处理@Value和@Autowired的BeanPostProcessor
        registry.registerBeanDefinition("cn.andl.springframework.context.annotation.internalAutowiredAnnotationProcessor",
                new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    /**
     * 处理bean的scope
     * @param beanDefinition bean的定义
     * @return bean的scope
     */
    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null) {
            return scope.value();
        }
        return StrUtil.EMPTY;
    }

    /**
     * 发现bean的名字
     * @param beanDefinition bean定义
     * @return bean的名字
     */
    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component annotation = beanClass.getAnnotation(Component.class);
        String value = annotation.value();
        // 没有定义时，用类名（小写第一个字母）代替
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }

}
