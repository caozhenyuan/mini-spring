package com.minis.web.servlet;

import com.minis.beans.BeansException;
import com.minis.web.WebApplicationContext;
import com.minis.web.WebBindingInitializer;
import com.minis.web.WebDataBinder;
import com.minis.web.WebDataBinderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author czy
 * @date 2023/04/03
 * @Deprecated 接受前端传 request、 response 与 handler，通过反射中的 invoke 调用方法并处理返回数据
 **/
public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private WebApplicationContext webApplicationContext;

    private WebBindingInitializer webBindingInitializer;

    public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        try {
            this.webBindingInitializer = (WebBindingInitializer) this.webApplicationContext.getBean("webBindingInitializer");
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        handlerInternal(request, response, (HandlerMethod) handler);
    }

    private void handlerInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        try {
            invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();

        Parameter[] methodParams = handler.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParams.length];
        int i = 0;
        //对调用方法里的每一个参数，处理绑定
        for (Parameter methodParameter : methodParams) {
            Object methodParamObj = methodParameter.getType().newInstance();
            //给这个参数创建WebDataBinder
            WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
            wdb.bind(request);
            methodParamObjs[i] = methodParamObj;
            i++;
        }
        Method invocableMethod = handler.getMethod();
        Object returnObj = invocableMethod.invoke(handler.getBean(), methodParamObjs);
        response.getWriter().append(returnObj.toString());
    }

    public WebBindingInitializer getWebBindingInitializer() {
        return webBindingInitializer;
    }

    public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer;
    }
}
