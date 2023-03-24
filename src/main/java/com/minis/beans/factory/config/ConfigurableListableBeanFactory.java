package com.minis.beans.factory.config;

import com.minis.beans.factory.ListableBeanFactory;

/**
 * @author 曹振远
 * @date 2023/03/22
 * @Deprecated 所有beanFactory的扩展
 **/
public interface ConfigurableListableBeanFactory extends
        ListableBeanFactory,
        AutowireCapableBeanFactory,
        ConfigurableBeanFactory {
}
