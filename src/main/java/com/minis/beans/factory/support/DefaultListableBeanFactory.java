package com.minis.beans.factory.support;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.AbstractAutowireCapableBeanFactory;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 曹振远
 * @date 2023/03/22
 * @Deprecated IOC引擎
 **/
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements ConfigurableListableBeanFactory {

    private ConfigurableListableBeanFactory parentBeanFactory;

    /**
     * 获取Bean定义的数量
     *
     * @return
     */
    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    /**
     * 获取所有Bean定义的ID集合
     *
     * @return
     */
    @Override
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionNames.toArray(new String[0]);
    }

    /**
     * 根据类型获取所属类型的所有BeanId
     *
     * @param type
     * @return
     */
    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> result = new ArrayList<>();
        for (String beanName : this.beanDefinitionNames) {
            BeanDefinition mbd = this.getBeanDefinition(beanName);
            Class<? extends BeanDefinition> classToMatch = mbd.getClass();
            if (type.isAssignableFrom(classToMatch)) {
                result.add(beanName);
            }

        }
        return result.toArray(new String[0]);
    }


    /**
     * 根据类型获取所有的Bean
     *
     * @param type
     * @return
     * @throws BeansException
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        String[] beanNames = getBeanNamesForType(type);
        Map<String, T> result = new LinkedHashMap<>();
        for (String beanName : beanNames) {
            Object beanInstance = getBean(beanName);
            result.put(beanName, (T) beanInstance);
        }
        return result;
    }

    public void setParent(ConfigurableListableBeanFactory beanFactory) {
        this.parentBeanFactory = beanFactory;
    }

    @Override
    public Object getBean(String beanName) throws BeansException{
        Object result = super.getBean(beanName);
        if (result == null) {
            result = this.parentBeanFactory.getBean(beanName);
        }

        return result;
    }
}
