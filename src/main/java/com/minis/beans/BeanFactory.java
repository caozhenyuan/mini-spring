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
    Object getBean(String beanName) throws BeansException;


    /**
     * 注册一个 Bean 实例
     *
     * @param beanDefinition
     */
    void registerBeanDefinition(BeanDefinition beanDefinition);

    /**
     * 对单例 Bean 的注册
     *
     * @param beanName Bean名称
     * @param obj      beanName 对应的 Bean 的信息
     */
    void registerBean(String beanName, Object obj);


    /**
     * 根据名称查询容器是否包含Bean实例
     *
     * @param name
     * @return
     */
    Boolean containsBean(String name);


    /**
     * 是否是Singleton
     *
     * @param name
     * @return
     */
    boolean isSingleton(String name);

    /**
     * 是否是Prototype
     *
     * @param name
     * @return
     */
    boolean isPrototype(String name);

    /**
     * 获取 Bean 的类型
     *
     * @param name
     * @return
     */
    Class getType(String name);
}
