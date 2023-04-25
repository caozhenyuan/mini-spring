package com.minis.web.method.annotation;

import com.minis.web.HttpMessageConverter;
import com.minis.web.WebDataBinder;
import com.minis.web.bind.annotation.ResponseBody;
import com.minis.web.bind.support.WebBindingInitializer;
import com.minis.web.bind.support.WebDataBinderFactory;
import com.minis.web.context.WebApplicationContext;
import com.minis.web.method.HandlerMethod;
import com.minis.web.servlet.HandlerAdapter;
import com.minis.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
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

    private HttpMessageConverter messageConverter;

    public RequestMappingHandlerAdapter() {
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return handlerInternal(request, response, (HandlerMethod) handler);
    }

    private ModelAndView handlerInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        try {
            return invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();

        Parameter[] methodParams = handler.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParams.length];
        int i = 0;
        //对调用方法里的每一个参数，处理绑定
        for (Parameter methodParameter : methodParams) {
            Class<?> type = methodParameter.getType();
            if (HttpServletRequest.class != type && HttpServletResponse.class != type) {
                Object methodParamObj = methodParameter.getType().newInstance();
                //给这个参数创建WebDataBinder
                WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
                wdb.bind(request);
                methodParamObjs[i] = methodParamObj;
            } else if (HttpServletRequest.class == type) {
                methodParamObjs[i] = request;
            } else if (HttpServletResponse.class == type) {
                methodParamObjs[i] = response;
            }
            i++;
        }
        Method invocableMethod = handler.getMethod();
        Object returnObj = invocableMethod.invoke(handler.getBean(), methodParamObjs);
        Class<?> returnType = invocableMethod.getReturnType();

        ModelAndView mav = null;
        if (invocableMethod.isAnnotationPresent(ResponseBody.class) && !(returnObj instanceof String)) { //ResponseBody
            this.messageConverter.write(returnObj, response);
        } else if (invocableMethod.isAnnotationPresent(ResponseBody.class) && returnObj instanceof String) {
            PrintWriter writer = response.getWriter();
            writer.write((String) returnObj);
            return null;
        } else if (void.class == returnType) {
            return null;
        } else {
            if (returnObj instanceof ModelAndView) {
                mav = (ModelAndView) returnObj;
            } else if (returnObj instanceof String) {
                String sTarget = (String) returnObj;
                mav = new ModelAndView();
                mav.setViewName(sTarget);
            }
        }
        return mav;
    }

    public WebBindingInitializer getWebBindingInitializer() {
        return webBindingInitializer;
    }

    public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer;
    }

    public HttpMessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(HttpMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }
}
