package com.minis.web.method.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author czy
 * @date 2023/04/03
 **/
public class MappingRegistry {

    /**
     * url名称集合
     */
    private List<String> urlMappingNames = new ArrayList<>();

    /**
     * url对应对象实例集合
     */
    private Map<String, Object> mappingObjs = new HashMap<>();

    /**
     * url对应方法集合
     */
    private Map<String, Method> mappingMethods = new HashMap<>();

    public List<String> getUrlMappingNames() {
        return urlMappingNames;
    }

    public void setUrlMappingNames(List<String> urlMappingNames) {
        this.urlMappingNames = urlMappingNames;
    }

    public Map<String, Object> getMappingObjs() {
        return mappingObjs;
    }

    public void setMappingObjs(Map<String, Object> mappingObjs) {
        this.mappingObjs = mappingObjs;
    }

    public Map<String, Method> getMappingMethods() {
        return mappingMethods;
    }

    public void setMappingMethods(Map<String, Method> mappingMethods) {
        this.mappingMethods = mappingMethods;
    }
}
