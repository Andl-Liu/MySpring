package cn.andl.springframework.context.annotation;

import cn.andl.springframework.beans.factory.config.BeanDefinition;
import cn.andl.springframework.stereotype.Component;
import cn.hutool.core.util.ClassUtil;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 类路径扫描 候选组件提供器
 */
public class ClassPathScanningCandidateComponentProvider {

    /**
     * 从指定包下，扫描带有Component注解的类
     * @param basePackage 指定包
     * @return 候选组件的bean定义
     */
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();

        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);

        for (Class<?> clazz : classes) {
            candidates.add(new BeanDefinition(clazz));
        }

        return candidates;
    }

}
