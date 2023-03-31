package com.minis.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 曹振远
 * @date 2023/03/27
 * @Deprecated 用来处理所有的 Web 请求
 **/
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 7778288198769546179L;

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

    private String sContextConfigLocation;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        //加载minisMVC-servlet.xml配置文件
        this.webApplicationContext = (WebApplicationContext) this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(sContextConfigLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        refresh();
    }

    /**
     * 初始化
     */
    protected void refresh() {
        // 初始化 controller
        initController();
        // 初始化 url 映射
        initMapping();
    }

    /**
     * 扫描到的每一个类进行加载和实例化，与类的名字建立映射关系
     */
    protected void initController() {
        //扫描包获取,所有的类名
        this.controllerNames = scanPackages(packageNames);
        for (String controllerName : controllerNames) {
            try {
                //保存类名和类的class映射
                Class<?> clz = Class.forName(controllerName);
                this.controllerClasses.put(controllerName, clz);
                //保存类名和类实例Bean的映射
                Object obj = clz.newInstance();
                this.controllerObjs.put(controllerName, obj);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 扫描包获取,所有的类名
     *
     * @param packageNames 包名集合
     * @return
     */
    private List<String> scanPackages(List<String> packageNames) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packageNames) {
            tempControllerNames.addAll(scanPackage(packageName));
        }
        return tempControllerNames;
    }

    /**
     * 使用递归遍历包路径下的每个文件
     *
     * @param packageName
     * @return
     */
    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames = new ArrayList<>();
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanPackage(packageName + "." + file.getName());
            } else {
                String controllerName = packageName + "." + file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }

    /**
     * 找到使用了注解 @RequestMapping 的方法，URL 存放到  urlMappingNames 里，
     * 映射的对象存放到  mappingObjs 里，映射的方法存放到  mappingMethods 里
     */
    protected void initMapping() {
        for (String controllerName : this.controllerNames) {
            Class<?> claszz = this.controllerClasses.get(controllerName);
            Object obj = this.controllerObjs.get(controllerName);
            Method[] methods = claszz.getMethods();
            if (null != methods) {
                //检查所有的方法
                for (Method method : methods) {
                    boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                    if (isRequestMapping) {
                        String methodName = method.getName();
                        //建立方法名和Url映射
                        String urlMapping = method.getAnnotation(RequestMapping.class).value();
                        this.urlMappingNames.add(urlMapping);
                        this.mappingObjs.put(urlMapping, obj);
                        this.mappingMethods.put(urlMapping, method);
                    }
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String sPath = request.getServletPath();
        if (!this.urlMappingNames.contains(sPath)) {
            return;
        }
        Method method = this.mappingMethods.get(sPath);
        Object obj = this.mappingObjs.get(sPath);
        Object objResult;
        try {
            objResult = method.invoke(obj);
            response.getWriter().append(objResult.toString());
        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
