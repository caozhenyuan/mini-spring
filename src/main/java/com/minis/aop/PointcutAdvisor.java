package com.minis.aop;

/**
 * @author czy
 * @date 2023/05/10
 **/
public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();
}
