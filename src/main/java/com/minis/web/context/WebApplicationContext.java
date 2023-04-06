package com.minis.web.context;

import com.minis.context.ApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author czy
 * @date 2023/03/31
 **/
public interface WebApplicationContext extends ApplicationContext {

    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

    ServletContext getServletContext();

    void setServletContext(ServletContext servletContext);
}
