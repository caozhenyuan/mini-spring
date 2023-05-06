package com.minis.aop;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.FactoryBean;
import com.minis.util.ClassUtils;

/**
 * @author czy
 * @date 2023/05/05
 **/
public class ProxyFactoryBean implements FactoryBean<Object>, BeanFactoryAware {

    private BeanFactory beanFactory;

    private AopProxyFactory aopProxyFactory;

    private String targetName;

    private Object target;

    private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();

    private Object singletonInstance;

    private String interceptorName;

    private Advisor advisor;

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
        initializeAdvisor();
        return getSingletonInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }


    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public AopProxyFactory getAopProxyFactory() {
        return aopProxyFactory;
    }

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        this.aopProxyFactory = aopProxyFactory;
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
        return getAopProxyFactory().createAopProxy(target, this.advisor);
    }

    public void setSingletonInstance(Object singletonInstance) {
        this.singletonInstance = singletonInstance;
    }


    /**
     * 将应用程序自定义的拦截器获取到 Advisor 里
     */
    private synchronized void initializeAdvisor() {
        Object advice = null;
        MethodInterceptor mi = null;
        try {
            advice = this.beanFactory.getBean(this.interceptorName);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        if (advice instanceof BeforeAdvice) {
            mi = new MethodBeforeAdviceInterceptor((MethodBeforeAdvice) advice);
        } else if (advice instanceof AfterAdvice) {
            mi = new AfterReturningAdviceInterceptor((AfterReturningAdvice) advice);
        } else if (advice instanceof MethodInterceptor) {
            mi = (MethodInterceptor) advice;
        }
        advisor = new DefaultAdvisor();
        advisor.setMethodInterceptor(mi);
    }
}
