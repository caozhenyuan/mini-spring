package com.minis.aop;

/**
 * @author czy
 * @date 2023/05/05
 **/
public interface Advisor {

    MethodInterceptor getMethodInterceptor();

    void setMethodInterceptor(MethodInterceptor methodInterceptor);

    Advice getAdvice();
}
