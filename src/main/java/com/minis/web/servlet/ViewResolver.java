package com.minis.web.servlet;

/**
 * @author czy
 * @date 2023/04/06
 * @Deprecated 根据某个规则或者是用户配置来确定 View 在哪里
 **/
public interface ViewResolver {

    View resolveViewName(String viewName) throws Exception;
}
