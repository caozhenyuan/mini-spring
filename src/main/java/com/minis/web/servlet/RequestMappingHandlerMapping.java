package com.minis.web.servlet;

import com.minis.web.RequestMapping;
import com.minis.web.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author czy
 * @date 2023/04/03
 * @Deprecated 遍历 WAC 中已经注册的所有的 Bean，并处理带有 @RequestMapping 注解的类，
 * 使用 mappingRegistry 存储 URL 地址与方法和实例的映射关系
 **/
public class RequestMappingHandlerMapping implements HandlerMapping {

    private WebApplicationContext webApplicationContext;

    private final MappingRegistry mappingRegistry = new MappingRegistry();

    public RequestMappingHandlerMapping(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        initMapping();
    }

    /**
     * 建立URL与调用方法和实例的映射关系，存储在MappingRegistry中
     */
    private void initMapping() {
        Class<?> clz = null;
        Object obj = null;
        String[] controllerNames = this.webApplicationContext.getBeanDefinitionNames();
        //扫描WAC中存放的所有bean
        for (String controllerName : controllerNames) {
            try {
                clz = Class.forName(controllerName);
                obj = this.webApplicationContext.getBean(controllerName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Method[] methods = clz.getDeclaredMethods();
            if (null != methods) {
                //检测每个方法声明
                for (Method method : methods) {
                    boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                    //如果该方法带有@RequestMapping注解，则建立映射关系
                    if (isRequestMapping) {
                        String urlMapping = method.getAnnotation(RequestMapping.class).value();
                        this.mappingRegistry.getUrlMappingNames().add(urlMapping);
                        this.mappingRegistry.getMappingObjs().put(urlMapping, obj);
                        this.mappingRegistry.getMappingMethods().put(urlMapping, method);
                    }
                }
            }
        }
    }

    /**
     * 根据访问URL查找对应的方法
     *
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        String sPath = request.getServletPath();
        if (!this.mappingRegistry.getUrlMappingNames().contains(sPath)) {
            return null;
        }
        Method method = this.mappingRegistry.getMappingMethods().get(sPath);
        Object obj = this.mappingRegistry.getMappingObjs().get(sPath);
        return new HandlerMethod(method, obj);
    }
}
