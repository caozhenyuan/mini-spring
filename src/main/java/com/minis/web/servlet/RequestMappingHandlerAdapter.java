package com.minis.web.servlet;

import com.minis.web.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author czy
 * @date 2023/04/03
 * @Deprecated 接受前端传 request、 response 与 handler，通过反射中的 invoke 调用方法并处理返回数据
 **/
public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private WebApplicationContext webApplicationContext;

    public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        handlerInternal(request, response, (HandlerMethod) handler);
    }

    private void handlerInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        Method method = handler.getMethod();
        Object obj = handler.getBean();
        Object result = null;
        try {
            result = method.invoke(obj);
            response.getWriter().append(result.toString());
        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
            e.printStackTrace();
        }
    }
}
