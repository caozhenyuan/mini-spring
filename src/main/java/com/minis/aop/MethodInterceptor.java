package com.minis.aop;

/**
 * @author czy
 * @date 2023/05/05
 * @Deprecated 调用方法上的拦截器，也就是它实现在某个方法上的增强
 **/
public interface MethodInterceptor extends Interceptor {

    Object invoke(MethodInvocation invocation) throws Throwable;
}
