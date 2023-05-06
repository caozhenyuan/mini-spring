package com.minis.aop;

import java.lang.reflect.Method;

/**
 * @author czy
 * @date 2023/05/05
 **/
public interface MethodInvocation {

    Method getMethod();

    Object[] getArguments();

    Object getThis();

    Object proceed() throws Throwable;
}
