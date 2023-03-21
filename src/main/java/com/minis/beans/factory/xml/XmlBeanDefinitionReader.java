package com.minis.beans.factory.xml;

import com.minis.beans.PropertyValues;
import com.minis.beans.SimpleBeanFactory;
import com.minis.beans.factory.config.AutowireCapableBeanFactory;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 曹振远
 * @date 2023/03/14
 **/
public class XmlBeanDefinitionReader {

    private AutowireCapableBeanFactory beanFactory;

    public XmlBeanDefinitionReader(AutowireCapableBeanFactory autowireCapableBeanFactory) {
        this.beanFactory = autowireCapableBeanFactory;
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

            //handle constructor
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues avs = new ConstructorArgumentValues();
            for (Element constructorElement : constructorElements) {
                String cType = constructorElement.attributeValue("type");
                String cName = constructorElement.attributeValue("name");
                String value = constructorElement.attributeValue("value");
                avs.addArgumentValue(cType, cName, value);
            }
            beanDefinition.setConstructorArgumentValues(avs);
            //end of handle constructor

            //handle properties
            List<Element> propertyElements = element.elements("property");
            PropertyValues pvs = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element propertyElement : propertyElements) {
                String pType = propertyElement.attributeValue("type");
                String pName = propertyElement.attributeValue("name");
                String pValue = propertyElement.attributeValue("value");

                String pRef = propertyElement.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                if (null != pValue && !"".equals(pValue)) {
                    pV = pValue;
                } else {
                    isRef = true;
                    //此处ref的值在代码里复制给value
                    pV = pRef;
                    refs.add(pRef);
                }
                pvs.addPropertyValue(pType, pName, pV, isRef);
            }
            beanDefinition.setPropertyValues(pvs);
            String[] refArrays = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArrays);
            //end of handle properties

            this.beanFactory.registerBeanDefinition(beanDefinition);
        }
    }
}
