package com.minis.aop;

import com.minis.beans.factory.FactoryBean;
import com.minis.util.ClassUtils;

/**
 * @author czy
 * @date 2023/05/05
 **/
public class ProxyFactoryBean implements FactoryBean<Object> {

    private AopProxyFactory aopProxyFactory;

    private String[] interceptorNames;

    private String targetName;

    private Object target;

    private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();

    private Object singletonInstance;

    public ProxyFactoryBean() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    /**
     * 获取内部对象
     *
     * @return
     * @throws Exception
     */
    @Override
    public Object getObejct() throws Exception {
        return getSingletonInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }


    public AopProxyFactory getAopProxyFactory() {
        return aopProxyFactory;
    }

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        this.aopProxyFactory = aopProxyFactory;
    }

    public String[] getInterceptorNames() {
        return interceptorNames;
    }

    public void setInterceptorNames(String[] interceptorNames) {
        this.interceptorNames = interceptorNames;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public ClassLoader getProxyClassLoader() {
        return proxyClassLoader;
    }

    public void setProxyClassLoader(ClassLoader proxyClassLoader) {
        this.proxyClassLoader = proxyClassLoader;
    }

    /**
     * 获取代理
     *
     * @return
     */
    private synchronized Object getSingletonInstance() {
        if (null == this.singletonInstance) {
            this.singletonInstance = getProxy(createAopProxy());
        }
        System.out.println("----------return proxy for :" + this.singletonInstance + "--------" + this.singletonInstance.getClass());
        return this.singletonInstance;
    }

    /**
     * 生成代理对象
     *
     * @param aopProxy
     * @return
     */
    protected Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }

    protected AopProxy createAopProxy() {
        System.out.println("----------createAopProxy for :" + target + "--------");
        return getAopProxyFactory().createAopProxy(target);
    }

    public void setSingletonInstance(Object singletonInstance) {
        this.singletonInstance = singletonInstance;
    }
}
