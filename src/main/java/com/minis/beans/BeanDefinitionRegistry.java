package com.minis.beans;

/**
 * @author 曹振远
 * @date 2023/03/15
 * @Deprecated 存放 BeanDefinition 的仓库
 **/
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String name, BeanDefinition db);

    void removeBeanDefinition(String name);

    BeanDefinition getBeanDefinition(String name);

    boolean containsBeanDefinition(String name);
}
