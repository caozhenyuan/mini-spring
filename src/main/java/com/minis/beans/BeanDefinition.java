package com.minis.beans;

/**
 * @author 曹振远
 * @date 2023/03/14
 * @Deprecated Bean 的定义
 **/
public class BeanDefinition {

    private String id;

    private String className;

    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getId() {
        return this.id;
    }

    public String getClassName() {
        return this.className;
    }
}
