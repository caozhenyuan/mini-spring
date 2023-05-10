package com.minis.aop;

import com.minis.util.PatternMatchUtils;

import java.lang.reflect.Method;

/**
 * @author czy
 * @date 2023/05/10
 **/
public class NameMatchMethodPointcut implements MethodMatcher, Pointcut {

    private String mappedName;

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return mappedName.equals(method.getName()) || isMatch(method.getName(), mappedName);
    }

    /**
     * 核心方法，判断方法名是否匹配给定的模式
     *
     * @param methodName
     * @param mappedName
     * @return
     */
    protected boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
}
