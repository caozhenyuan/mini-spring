package com.minis.context;

import com.minis.beans.*;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

/**
 * @author 曹振远
 * @date 2023/03/14
 * 解析 XML 文件中的内容。加载解析的内容，构建 BeanDefinition。
 * 读取 BeanDefinition 的配置信息，实例化 Bean，
 * 然后把它注入到 BeanFactory 容器中。
 **/
public class ClassPathXmlApplicationContext implements BeanFactory {

    private BeanFactory beanFactory;

    /**
     * context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactor
     */
    ClassPathXmlApplicationContext(String fileName) {
        Resource resource = new ClassPathXmlResource(fileName);
        BeanFactory beanFactory = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
    }

    /**
     * context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
     *
     * @param beanName bean名称
     * @return bean实例
     */
    @Override
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException {
        return this.beanFactory.getBean(beanName);
    }

    /**
     * 注册一个bean实例
     *
     * @param beanDefinition Bean实例
     */
    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanFactory.registerBeanDefinition(beanDefinition);
    }
}
