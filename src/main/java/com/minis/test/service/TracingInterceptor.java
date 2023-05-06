package com.minis.test.service;

import com.minis.aop.MethodInterceptor;
import com.minis.aop.MethodInvocation;

import java.util.Arrays;

/**
 * @author czy
 * @date 2023/05/06
 **/
public class TracingInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation i) throws Throwable {
        System.out.println("method " + i.getMethod() + " is called on " + i.getThis() + " with args " + Arrays.toString(i.getArguments()));
        //执行拦截的方法
        Object ret = i.proceed();
        System.out.println("method " + i.getMethod() + " returns " + ret);
        return ret;
    }
}
