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

    Advisor advisor;

    public JdkDynamicAopProxy(Object target, Advisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(JdkDynamicAopProxy.class.getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("doAction".equals(method.getName())) {
            Class<?> targetClass = null != target ? target.getClass() : null;
            MethodInterceptor methodInterceptor = this.advisor.getMethodInterceptor();
            MethodInvocation invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass);
            return methodInterceptor.invoke(invocation);
        }
        return null;
    }
}
