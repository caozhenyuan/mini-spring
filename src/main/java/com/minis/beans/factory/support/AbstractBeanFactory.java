package com.minis.beans.factory.support;

import com.minis.beans.BeansException;
import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConfigurableBeanFactory;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 曹振远
 * @date 2023/03/21
 * @Deprecated 抽象出来的公共方法的工厂对象
 **/
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory, BeanDefinitionRegistry {

    protected Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    protected List<String> beanDefinitionNames = new ArrayList<>();

    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    public AbstractBeanFactory() {

    }

    /**
     * 刷新容器
     */
    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getBean(String beanName) throws BeansException {
        //先尝试直接从容器中拿bean实例
        Object singleton = this.getSingleton(beanName);
        if (null == singleton) {
            //如果没有实例，则尝试从毛坯实例中中获取
            singleton = this.earlySingletonObjects.get(beanName);
            //如果连毛坯都没有，则尝试创建bean实例并注册
            if (null == singleton) {
                BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
                if (null == beanDefinition) {
                    throw new BeansException("No bean.");
                }
                singleton = createBean(beanDefinition);
                this.registerSingleton(beanName, singleton);
                //预留BeanPostProcessor位置
                //step1: postProcessorBeforeInitialization
                applyBeanPostProcessorsBeforeInitialization(singleton, beanName);
                //step2: afterPropertiesSet
                //step3: init-method
                if (null != beanDefinition.getInitMethodName() && !"".equals(beanDefinition)) {
                    invokeInitMethod(beanDefinition, singleton);
                }
                //step4: postProcessAfterInitialization
                applyBeanPostProcessorsAfterInitialization(singleton, beanName);
            }
        }
        return singleton;
    }

    /**
     * 初始化方法
     *
     * @param beanDefinition Bean的定义信息
     * @param singleton      Bean实例
     */
    private void invokeInitMethod(BeanDefinition beanDefinition, Object singleton) {
        Class<? extends BeanDefinition> clz = beanDefinition.getClass();
        try {
            Method method = clz.getMethod(beanDefinition.getInitMethodName());
            method.invoke(singleton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean containsBean(String name) {
        return containsSingleton(name);
    }

    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
        if (!beanDefinition.isLazyInit()) {
            try {
                getBean(name);
            } catch (BeansException e) {
                e.printStackTrace();
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

    /**
     * 创建Bean
     *
     * @param beanDefinition Bean的定义信息
     * @return
     */
    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        //创建毛坯bean实例
        Object obj = doCreateBean(beanDefinition);
        //存放在毛坯实例缓存中
        this.earlySingletonObjects.put(beanDefinition.getId(), obj);
        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //处理属性
        handleProperties(beanDefinition, clz, obj);
        return obj;
    }

    /**
     * 创建毛胚实例，仅仅调用构造方法，没有进行属性处理
     *
     * @param bd
     * @return
     */
    private Object doCreateBean(BeanDefinition bd) {
        Class<?> clz;
        Object obj = null;
        Constructor con;

        try {
            clz = Class.forName(bd.getClassName());
            //处理构造器参数
            ConstructorArgumentValues argumentValues = bd.getConstructorArgumentValues();
            //如果有参数
            if (!argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];
                //对每一个参数，分数据类型分别处理
                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
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
                        //默认String
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                }
                //按照特定构造器创建实例
                con = clz.getConstructor(paramTypes);
                obj = con.newInstance(paramValues);
            } else {
                //如果没有参数，直接创建实例
                obj = clz.newInstance();
            }
            System.out.println(bd.getId() + " bean created. " + bd.getClassName() + " : " + obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }


    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {
        //处理属性
        System.out.println("handle properties for bean: " + bd.getId());
        //如果有属性
        PropertyValues propertyValues = bd.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            try {
                //对每一个属性，分数据类型分别处理
                for (int i = 0; i < propertyValues.size(); i++) {
                    PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                    String pName = propertyValue.getName();
                    String pType = propertyValue.getType();
                    Object pValue = propertyValue.getValue();
                    boolean isRef = propertyValue.getIsRef();

                    Class<?>[] paramTypes = new Class<?>[1];
                    Object[] paramValues = new Object[1];
                    if (!isRef) {
                        //如果不是ref，只是普通属性，对每一个属性，分数据类型分别处理
                        if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                            paramTypes[0] = String.class;
                        } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                            paramTypes[0] = Integer.class;
                        } else if ("int".equals(pType) || "java.lang.int".equals(pType)) {
                            paramTypes[0] = int.class;
                        } else {
                            paramTypes[0] = String.class;
                        }
                        paramValues[0] = pValue;
                    } else {
                        //is ref ,create the dependent beans
                        paramTypes[0] = Class.forName(pType);
                        //再次调用getBean创建ref的bean实例
                        paramValues[0] = getBean((String) pValue);
                    }
                    //按照setXxxx规范查到set方法，调用setter方法设置属性
                    String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);
                    Method method = clz.getMethod(methodName, paramTypes);
                    method.invoke(obj, paramValues);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    abstract public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    abstract public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
