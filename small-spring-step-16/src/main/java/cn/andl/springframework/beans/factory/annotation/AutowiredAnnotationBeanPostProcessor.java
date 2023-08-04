package cn.andl.springframework.beans.factory.annotation;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.PropertyValues;
import cn.andl.springframework.beans.factory.BeanFactory;
import cn.andl.springframework.beans.factory.BeanFactoryAware;
import cn.andl.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.andl.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.andl.springframework.utils.ClassUtils;
import cn.hutool.core.bean.BeanUtil;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        // 获取bean对应的class
        Class<?> clazz = bean.getClass();
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;

        // 获取所有的字段
        Field[] declaredFields = clazz.getDeclaredFields();

        // 遍历所有字段，处理@Value
        for (Field field : declaredFields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation == null) {
                continue;
            }
            // 获取注解中的值
            String value = valueAnnotation.value();
            // 解析获取到的值
            value = beanFactory.resolveEmbeddedValue(value);
            // 将值填充到bean的对应字段中
            BeanUtil.setFieldValue(bean, field.getName(), value);
        }

        // 遍历所有字段，处理@Autowried和@Qualifier
        for (Field field : declaredFields) {
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (autowiredAnnotation == null) {
                continue;
            }
            // 获取字段的类型
            Class<?> fieldType = field.getType();
            // 依赖的bean
            Object dependentBean = null;

            // 判断是否存在指定的bean名称
            Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
            if (qualifierAnnotation == null) {
                // 通过类型获取bean
                dependentBean = beanFactory.getBean(fieldType);
            } else {
                // 通过名称和类型获取bean
                dependentBean = beanFactory.getBean(qualifierAnnotation.value(), fieldType);
            }
            // 将值填充到bean的对应字段中
            BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
        }

        return new PropertyValues();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }
}
