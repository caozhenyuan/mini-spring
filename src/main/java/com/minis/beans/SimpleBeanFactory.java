package com.minis.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 曹振远
 * @date 2023/03/14
 **/
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    private List<String> beanDefinitionNames = new ArrayList<>();

    public SimpleBeanFactory() {

    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        //先尝试直接拿bean实例
        Object singleton = this.getSingleton(beanName);
        //如果此时还没有这个bean实例，则获取它的定义来创建实例
        if (null == singleton) {
            //获取bean的定义
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (null == beanDefinition) {
                throw new BeansException("No bean.");
            }
            singleton = createBean(beanDefinition);
            //注册这个bean实例
            this.registerBean(beanName, singleton);
            //bean初始化
            if (null != beanDefinition.getInitMethodName()) {
                //init method
            }
            if (null == singleton) {
                throw new BeansException("bean is null.");
            }
        }
        return singleton;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanDefinition.getId(), beanDefinition);
    }

    /**
     * 对单例 Bean 的注册
     *
     * @param beanName Bean名称
     * @param obj      beanName 对应的 Bean 的信息
     */
    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);
    }

    /**
     * 对单例bean的操作
     *
     * @param name
     * @return
     */
    @Override
    public Boolean containsBean(String name) {
        return containsSingleton(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    @Override
    public Class getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition db) {
        this.beanDefinitionMap.put(name, db);
        this.beanDefinitionNames.add(name);
        if (!db.isLazyInit()) {
            try {
                getBean(name);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }


    /**
     * 创建Bean
     *
     * @param bd Bean的定义信息
     * @return
     */
    private Object createBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = null;
        Constructor con = null;

        try {
            clz = Class.forName(bd.getClassName());
            //handle constructor
            ArgumentValues argumentValues = bd.getConstructorArgumentValues();
            if (!argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];
                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    } else if ("Integer".equals(argumentValue.getType()) || "java.lang.Ingeter".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else if ("int".equals(argumentValue.getType()) || "java.lang.int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue()).intValue();
                    } else {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                    con = clz.getConstructor(paramTypes);
                    obj = con.newInstance(paramValues);
                }
            } else {
                obj = clz.newInstance();
            }

            //handle properties
            PropertyValues propertyValues = bd.getPropertyValues();
            if (!propertyValues.isEmpty()) {
                for (int i = 0; i < propertyValues.size(); i++) {
                    PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                    String pName = propertyValue.getName();
                    String pType = propertyValue.getType();
                    Object pValue = propertyValue.getValue();

                    Class<?>[] paramTypes = new Class<?>[1];
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(pType) || "java.lang.int".equals(pType)) {
                        paramTypes[0] = int.class;
                    } else {
                        paramTypes[0] = String.class;
                    }
                    Object[] paramValues = new Object[1];
                    paramValues[0] = pValue;

                    String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);
                    Method method = clz.getMethod(methodName, paramTypes);
                    method.invoke(obj, paramValues);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
