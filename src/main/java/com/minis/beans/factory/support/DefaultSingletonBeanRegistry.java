package com.minis.beans.factory.support;

import com.minis.beans.factory.config.SingletonBeanRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 曹振远
 * @date 2023/03/15
 **/
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    //容器中存放所有bean的名称的列表
    protected List<String> beanNames = new ArrayList<>();
    //容器中存放所有bean实例的Map。
    protected Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    protected Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);

    protected Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            Object oldObject = this.singletonObjects.get(beanName);
            if (null != oldObject) {
                throw new IllegalStateException("Could not register object [" + singletonObject +
                        "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
            }
            this.singletonObjects.put(beanName, singletonObject);
            this.beanNames.add(beanName);
            System.out.println(" bean registered............. " + beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }

    @Override
    public Boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return (String[]) this.beanNames.toArray();
    }

    /**
     * 根据Bean的名称删除单例Bean对象
     *
     * @param beanName
     */
    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.beanNames.remove(beanName);
        }
    }

    /**
     * 注册依赖Bean
     *
     * @param beanName
     * @param dependentBeanName
     */
    protected void registerDependentBean(String beanName, String dependentBeanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (null != dependentBeans && dependentBeans.contains(dependentBeanName)) {
            return;
        }
        //No entry yet->fully synchronized manipulation of the dependentBeans Set
        synchronized (this.dependentBeanMap) {
            dependentBeans = this.dependentBeanMap.get(beanName);
            if (null == dependentBeans) {
                dependentBeans = new LinkedHashSet<>(8);
                this.dependentBeanMap.put(beanName, dependentBeans);
            }
            dependentBeans.add(dependentBeanName);
        }
        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
            if (null == dependenciesForBean) {
                dependenciesForBean = new LinkedHashSet<>(8);
                this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
            }
            dependenciesForBean.add(beanName);
        }
    }

    /**
     * 判断该Bean是否依赖于其它的Bean
     *
     * @param beanName
     * @return
     */
    protected boolean hasDependentBean(String beanName) {
        return this.dependentBeanMap.containsKey(beanName);
    }

    /**
     * 获取该Bean的所有依赖Bean
     *
     * @param beanName
     * @return
     */
    protected String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans.isEmpty()) {
            return new String[0];
        }
        return dependentBeans.toArray(new String[0]);
    }

    /**
     * 获取该Bean都被哪些Bean所依赖
     *
     * @param beanName
     * @return
     */
    protected String[] getDependenciesForBean(String beanName) {
        Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean.isEmpty()) {
            return new String[0];
        }
        return dependenciesForBean.toArray(new String[0]);
    }
}
