package com.minis.beans;

import com.minis.core.Resource;
import org.dom4j.Element;


/**
 * @author 曹振远
 * @date 2023/03/14
 **/
public class XmlBeanDefinitionReader {

    private BeanFactory beanFactory;

    public XmlBeanDefinitionReader(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 解析的 XML 内容转换成 BeanDefinition，并加载到 BeanFactory 中
     */
    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);
            beanFactory.registerBeanDefinition(beanDefinition);
        }
    }
}
