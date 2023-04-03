package com.minis.web.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author czy
 * @date 2023/04/03
 **/
public interface HandlerMapping {
    /**
     * URL映射到某个实例方法
     *
     * @param request
     * @return
     * @throws Exception
     */
    HandlerMethod getHandler(HttpServletRequest request) throws Exception;
}
