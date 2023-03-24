package com.minis.beans.factory.config;

/**
 * @author 曹振远
 * @date 2023/03/15
 **/
public interface SingletonBeanRegistry {

    /**
     * 单例的注册
     *
     * @param beanName
     * @param singletonObject
     */
    void registerSingleton(String beanName, Object singletonObject);

    /**
     * 单例的获取
     *
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName);

    /**
     * 单例判断是否存在
     *
     * @param beanName
     * @return
     */
    boolean containsSingleton(String beanName);

    /**
     * 获取所有的单例 Bean 的名称
     *
     * @return
     */
    String[] getSingletonNames();
}
