package com.minis.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 曹振远
 * @date 2023/03/14
 **/
public class SimpleBeanFactory implements BeanFactory {

    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private List<String> beanNames = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();

    public SimpleBeanFactory() {

    }

    /**
     * getBean,容器的核心方法
     *
     * @param beanName bean名称
     * @return Bean实例
     */
    @Override
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException {
        //先尝试获取
        Object singleton = singletons.get(beanName);
        //如果此时还没有这个Bean的实力，则获取它的定义信息
        if (null == singleton) {
            int i = beanName.indexOf(beanName);
            if (-1 == i) {
                throw new NoSuchBeanDefinitionException();
            } else {
                //获取Bean的定义信息
                BeanDefinition beanDefinition = beanDefinitions.get(i);
                try {
                    singleton = Class.forName(beanDefinition.getClassName()).newInstance();
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                //注册Bean实例
                singletons.put(beanDefinition.getId(), singleton);
            }
        }
        return singleton;
    }

    /**
     * 注册一个 Bean 实例
     *
     * @param beanDefinition Bean实例
     */
    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitions.add(beanDefinition);
        this.beanNames.add(beanDefinition.getId());
    }
}
