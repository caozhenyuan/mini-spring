package com.minis.web.context;

import com.minis.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author czy
 * @date 2023/03/31
 * @Deprecated 当 Sevlet 服务器启动时，Listener 会优先启动，读配置文件路径，启动过程中初始化上下文，然后启动 IoC 容器，
 * 这个容器通过 refresh() 方法加载所管理的 Bean 对象。这样就实现了 Tomcat 启动的时候同时启动 IoC 容器
 **/
public class ContextLoaderListener implements ServletContextListener {
    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

    private WebApplicationContext context;

    public ContextLoaderListener() {

    }

    public ContextLoaderListener(WebApplicationContext context) {
        this.context = context;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        initWebApplicationContext(event.getServletContext());
    }

    private void initWebApplicationContext(ServletContext servletContext) {
        //此处是读取Bean的配置文件执行容器刷新
        String sContextLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        WebApplicationContext wac = new XmlWebApplicationContext(sContextLocation);
        wac.setServletContext(servletContext);
        this.context = wac;
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
