package com.minis.beans.factory.config;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;

/**
 * @author 曹振远
 * @date 2023/03/21
 * @Deprecated Bean的后置处理器接口
 **/
public interface BeanPostProcessor {

    /**
     * Bean初始化之前
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * Bean初始化之后
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;


    void setBeanFactory(BeanFactory beanFactory);
}
