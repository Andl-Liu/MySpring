package cn.andl.springframework.beans.factory.xml;

import cn.andl.springframework.beans.BeansException;
import cn.andl.springframework.beans.PropertyValue;
import cn.andl.springframework.beans.factory.config.BeanDefinition;
import cn.andl.springframework.beans.factory.config.BeanReference;
import cn.andl.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import cn.andl.springframework.beans.factory.support.BeanDefinitionRegistry;
import cn.andl.springframework.core.io.Resource;
import cn.andl.springframework.core.io.ResourceLoader;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

/**
 * XMLBean定义读取器
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry, ResourceLoader resourceLoader) {
        super(beanDefinitionRegistry, resourceLoader);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        super(beanDefinitionRegistry);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            try (InputStream inputStream = resource.getInputStream()) {
                doLoadBeanDefinitions(inputStream);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for(Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    /**
     * 从xml输入流中读取Bean定义，并用Bean注册器注册到Bean定义中
     * @param inputStream xml输入流
     */
    private void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        Document doc = XmlUtil.readXML(inputStream);
        Element root = doc.getDocumentElement();
        NodeList childNodes = root.getChildNodes();

        // 遍历所有子节点
        for (int i = 0;i < childNodes.getLength();i++) {
            // 当节点不是 Element 的时候跳过
            if (!(childNodes.item(i) instanceof Element)) {
                continue;
            }
            // 当节点名不是 bean 的时候跳过
            if (!("bean".equals(childNodes.item(i).getNodeName()))) {
                continue;
            }

            // 解析标签
            Element bean = (Element) childNodes.item(i);
            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            Class<?> clazz = Class.forName(className);

            // 设置bean的名字 id > name > className
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)) {
                beanName = clazz.getName();
            }

            // 判断Bean容器中是否已经有同名的Bean
            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }

            // 定义Bean
            BeanDefinition beanDefinition = new BeanDefinition(clazz);

            // 读取属性并填充
            for (int j = 0;j < bean.getChildNodes().getLength();j++) {
                // 当节点不是 Element 的时候跳过
                if (!(bean.getChildNodes().item(j) instanceof Element)) {
                    continue;
                }
                // 当节点名不是 property 的时候跳过
                if (!("property".equals(bean.getChildNodes().item(j).getNodeName()))) {
                    continue;
                }
                // 解析标签 property
                Element property = (Element) bean.getChildNodes().item(j);
                String attrName = property.getAttribute("name");
                String attrValue = property.getAttribute("value");
                String attrRef = property.getAttribute("ref");
                // 获取属性值
                Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
                // 添加到Bean定义中
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            // 将bean定义注册进容器中
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }

    }
}
