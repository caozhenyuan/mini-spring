package com.minis.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author czy
 * @date 2023/04/03
 **/
public interface HandlerAdapter {

    /**
     * 映射后对方法的调用（对象调用方法）
     *
     * @param request
     * @param response
     * @param obj
     */
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object obj);
}
