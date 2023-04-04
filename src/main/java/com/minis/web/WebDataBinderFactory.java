package com.minis.web;

import javax.servlet.http.HttpServletRequest;

/**
 * @author czy
 * @date 2023/04/04
 **/
public class WebDataBinderFactory {

    public WebDataBinder createBinder(HttpServletRequest request, Object target, String objectName) {
        WebDataBinder webDataBinder = new WebDataBinder(target, objectName);
        initBinder(webDataBinder, request);
        return webDataBinder;
    }

    private void initBinder(WebDataBinder webDataBinder, HttpServletRequest request) {

    }
}
