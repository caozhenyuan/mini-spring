package com.minis.beans;

/**
 * @author 曹振远
 * @date 2023/03/14
 **/
public interface BeanFactory {

    /**
     * 获取一个 Bean 实例
     *
     * @param beanName bean名称
     * @return Bean实例
     */
    Object getBean(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * 注册一个Bean实例
     *
     * @param beanDefinition Bean实例
     */
    void registerBeanDefinition(BeanDefinition beanDefinition);
}
