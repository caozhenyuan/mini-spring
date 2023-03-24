package com.minis.beans.factory.config;

import com.minis.beans.factory.BeanFactory;

/**
 * @author 曹振远
 * @date 2023/03/22
 **/
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    int getBeanPostProcessorCount();

    void registerDependentBean(String beanName, String dependentBeanName);

    String[] getDependentBeans(String beanName);

    String[] getDependenciesForBean(String beanName);
}
