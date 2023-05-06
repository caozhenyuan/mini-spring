package com.minis.aop;

/**
 * @author czy
 * @date 2023/05/06
 **/
public class AfterReturningAdviceInterceptor implements MethodInterceptor {

    public final AfterReturningAdvice advice;

    public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object proceed = mi.proceed();
        this.advice.afterReturning(proceed, mi.getMethod(), mi.getArguments(), mi.getThis());
        return proceed;
    }
}
