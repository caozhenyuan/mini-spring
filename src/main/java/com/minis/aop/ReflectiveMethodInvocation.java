package com.minis.aop;

import java.lang.reflect.Method;

/**
 * @author czy
 * @date 2023/05/06
 **/
public class ReflectiveMethodInvocation implements MethodInvocation {
    protected final Object proxy;
    protected final Object target;
    protected final Method method;
    protected Object[] arguments;
    private Class<?> targetClass;

    public ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        this.targetClass = targetClass;
    }

    public Object getProxy() {
        return proxy;
    }

    public Object getTarget() {
        return target;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public Object proceed() throws Throwable {
        return this.method.invoke(target, arguments);
    }
}
