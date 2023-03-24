package com.minis.context;

import com.minis.beans.BeansException;
import com.minis.beans.factory.ListableBeanFactory;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.ConfigurableBeanFactory;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.core.env.Environment;
import com.minis.core.env.EnvironmentCapable;

/**
 * @author 曹振远
 * @date 2023/03/23
 * @Deprecated 作为公共接口，所有的上下文都实现自ApplicationContext，支持上下文环境和事件发布
 **/
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, ConfigurableBeanFactory, ApplicationEventPublisher {

    String getApplicationName();

    long getStartupDate();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    void setEnvironment(Environment environment);

    Environment getEnvironment();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

    void refresh() throws BeansException, IllegalStateException;

    void close();

    boolean isActive();
}
