package com.minis.aop;

/**
 * @author czy
 * @date 2023/05/05
 **/
public class DefaultAopProxyFactory implements AopProxyFactory {
    @Override
    public AopProxy createAopProxy(Object target) {
        return new JdkDynamicAopProxy(target);
    }
}
