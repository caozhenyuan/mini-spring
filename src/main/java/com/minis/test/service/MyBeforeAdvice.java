package com.minis.test.service;

import com.minis.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * @author czy
 * @date 2023/05/06
 **/
public class MyBeforeAdvice implements MethodBeforeAdvice {
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("----------my interceptor before method call----------");
    }
}
