package com.minis.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author czy
 * @date 2023/05/05
 **/
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    Object target;

    PointcutAdvisor advisor;

    public JdkDynamicAopProxy(Object target, PointcutAdvisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(JdkDynamicAopProxy.class.getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> targetClass = null != target ? target.getClass() : null;
        if (this.advisor.getPointcut().getMethodMatcher().matches(method,targetClass)) {
            MethodInterceptor methodInterceptor = this.advisor.getMethodInterceptor();
            MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);
            return methodInterceptor.invoke(invocation);
        }
        return null;
    }
}
