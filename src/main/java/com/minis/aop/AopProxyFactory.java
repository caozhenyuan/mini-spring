package com.minis.aop;

/**
 * @author czy
 * @date 2023/05/05
 **/
public interface AopProxyFactory {

    AopProxy createAopProxy(Object target);
}
