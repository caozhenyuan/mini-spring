package com.minis.beans.factory.support;

import com.minis.beans.BeansException;
import com.minis.beans.factory.FactoryBean;

/**
 * @author czy
 * @date 2023/04/28
 **/
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    /**
     * 获取FactoryBean的类型
     *
     * @param factoryBean FactoryBean
     * @return 类型
     */
    protected Class<?> getTypeForFactoryBean(final FactoryBean<?> factoryBean) {
        return factoryBean.getObjectType();
    }

    protected Object getObjectFromFactoryBean(FactoryBean<?> factoryBean, String beanName) {
        Object object = doGetObjectFromFactoryBean(factoryBean, beanName);
        try {
            object = postProcessObjectFromFactoryBean(object, beanName);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        return object;
    }

    private Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
        return object;
    }

    /**
     * 从factory bean中获取内部包含的对象
     *
     * @param factoryBean factory bean
     * @param beanName    bean名称
     * @return bean target对象
     */
    private Object doGetObjectFromFactoryBean(FactoryBean<?> factoryBean, final String beanName) {
        Object object = null;
        try {
            object = factoryBean.getObejct();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        if(!(beanInstance instanceof FactoryBean)){
            return beanInstance;
        }
        FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
        return getObjectFromFactoryBean(factory,beanName);
    }
}
