package com.minis.web.servlet;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.AnnotationConfigWebApplicationContext;
import com.minis.web.WebApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 曹振远
 * @date 2023/03/27
 * @Deprecated 用来处理所有的 Web 请求
 **/
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 7778288198769546179L;

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

    /**
     * 用于存储需要扫描的package列表
     */
    private List<String> packageNames = new ArrayList<>();

    /**
     * 用于存储controller的名称与对象的映射关系
     */
    private Map<String, Object> controllerObjs = new HashMap<>();

    /**
     * 用于存储controller名称数组列表
     */
    private List<String> controllerNames = new ArrayList<>();

    /**
     * 用于存储controller名称与类的映射关系
     */
    private Map<String, Class<?>> controllerClasses = new HashMap<>();

    /**
     * 保存自定义的@RequestMapping名称（URL的名称）的列表
     */
    private List<String> urlMappingNames = new ArrayList<>();

    /**
     * 保存URL名称与对象的映射关系
     */
    private Map<String, Object> mappingObjs = new HashMap<>();

    /**
     * 保存URL名称与方法的映射关系
     */
    private Map<String, Method> mappingMethods = new HashMap<>();

    private WebApplicationContext webApplicationContext;

    private WebApplicationContext parentApplicationContext;

    private String sContextConfigLocation;

    private RequestMappingHandlerMapping handlerMapping;
    private RequestMappingHandlerAdapter handlerAdapter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //加载minisMVC-servlet.xml配置文件
        this.parentApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        this.webApplicationContext = new AnnotationConfigWebApplicationContext(sContextConfigLocation,
                this.parentApplicationContext);
        refresh();
    }

    /**
     * 初始化
     */
    protected void refresh() {
        // 初始化 controller
        initController();

        initHandlerMappings(this.webApplicationContext);
        initHandlerAdapters(this.parentApplicationContext);
    }

    private void initHandlerMappings(WebApplicationContext webApplicationContext) {
        this.handlerMapping = new RequestMappingHandlerMapping(webApplicationContext);
    }

    private void initHandlerAdapters(WebApplicationContext webApplicationContext) {
        this.handlerAdapter = new RequestMappingHandlerAdapter(webApplicationContext);
    }

    /**
     * 扫描到的每一个类进行加载和实例化，与类的名字建立映射关系
     */
    protected void initController() {
        //扫描包获取,所有的类名
        this.controllerNames = Arrays.asList(this.webApplicationContext.getBeanDefinitionNames());
        for (String controllerName : controllerNames) {
            try {
                //保存类名和类的class映射
                Class<?> clz = Class.forName(controllerName);
                this.controllerClasses.put(controllerName, clz);
                //保存类名和类实例Bean的映射
                Object obj = clz.newInstance();
                populateBean(obj);
                this.controllerObjs.put(controllerName, obj);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 处理controller中的@Autowired注解
     *
     * @param obj
     */
    private void populateBean(Object obj) {
        Class<?> clz = obj.getClass();
        Field[] fields = clz.getDeclaredFields();
        if (fields.length <= 0) {
            return;
        }
        for (Field field : fields) {
            boolean isAutowired = field.isAnnotationPresent(Autowired.class);
            if (isAutowired) {
                String fieldName = field.getName();
                Object bean;
                try {
                    bean = this.webApplicationContext.getBean(fieldName);
                    field.setAccessible(true);
                    field.set(obj, bean);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);
        try {
            doDisPath(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDisPath(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handler = this.handlerMapping.getHandler(request);
        if (null == handler) {
            return;
        }
        this.handlerAdapter.handle(request, response, handler);
    }
}
