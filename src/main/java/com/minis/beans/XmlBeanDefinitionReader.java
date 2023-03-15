package com.minis.beans;

import com.minis.core.Resource;
import org.dom4j.Element;

import java.util.List;


/**
 * @author 曹振远
 * @date 2023/03/14
 **/
public class XmlBeanDefinitionReader {

    private SimpleBeanFactory simpleBeanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
        this.simpleBeanFactory = simpleBeanFactory;
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

            //handle properties
            List<Element> propertyElements = element.elements("properties");
            PropertyValues pvs = new PropertyValues();
            for (Element propertyElement : propertyElements) {
                String pType = propertyElement.attributeValue("type");
                String pName = propertyElement.attributeValue("name");
                String pValue = propertyElement.attributeValue("value");
                pvs.addPropertyValue(pType, pName, pValue);
            }
            beanDefinition.setPropertyValues(pvs);
            //end of handle properties

            //handle constructor
            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues avs = new ArgumentValues();
            for (Element constructorElement : constructorElements) {
                String cType = constructorElement.attributeValue("type");
                String cName = constructorElement.attributeValue("name");
                String value = constructorElement.attributeValue("value");
                avs.addArgumentValue(cType, cName, value);
            }
            beanDefinition.setConstructorArgumentValues(avs);
            //end of handle constructor

            this.simpleBeanFactory.registerBeanDefinition(beanDefinition);
        }
    }
}
