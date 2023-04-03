package com.minis.web;

import com.minis.context.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author czy
 * @date 2023/04/03
 **/
public class XmlWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {


    private ServletContext servletContext;


    public XmlWebApplicationContext(String fileName) {
        super(fileName);
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
