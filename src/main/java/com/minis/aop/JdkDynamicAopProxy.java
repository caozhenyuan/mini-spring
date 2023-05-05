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

    public JdkDynamicAopProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(JdkDynamicAopProxy.class.getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("doAction".equals(method.getName())) {
            System.out.println("-----before call real object, dynamic proxy........");
            return method.invoke(target, args);
        }
        return null;
    }
}
