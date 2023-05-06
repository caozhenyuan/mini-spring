package com.minis.aop;

import java.lang.reflect.Method;

/**
 * @author czy
 * @date 2023/05/06
 **/
public interface AfterReturningAdvice extends AfterAdvice{

    void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable;
}
