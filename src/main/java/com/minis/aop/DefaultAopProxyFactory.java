package com.minis.aop;

/**
 * @author czy
 * @date 2023/05/05
 **/
public class DefaultAopProxyFactory implements AopProxyFactory {
    @Override
    public AopProxy createAopProxy(Object target, PointcutAdvisor advisor) {
        return new JdkDynamicAopProxy(target, advisor);
    }
}
