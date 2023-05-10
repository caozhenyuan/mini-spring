package com.minis.aop;

import java.lang.reflect.Method;

/**
 * @author czy
 * @date 2023/05/10
 * @Deprecated 方法的匹配算法
 **/
public interface MethodMatcher {

    boolean matches(Method method, Class<?> targetClass);
}
