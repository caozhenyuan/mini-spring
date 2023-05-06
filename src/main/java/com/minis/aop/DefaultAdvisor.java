package com.minis.aop;

/**
 * @author czy
 * @date 2023/05/06
 **/
public class DefaultAdvisor implements Advisor {


    private MethodInterceptor methodInterceptor;

    public DefaultAdvisor() {
    }

    @Override
    public MethodInterceptor getMethodInterceptor() {
        return this.methodInterceptor;
    }

    @Override
    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }
}
