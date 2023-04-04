package com.minis.web;

import com.minis.beans.PropertyEditor;
import com.minis.beans.PropertyEditorRegistrySupport;
import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author czy
 * @date 2023/04/04
 **/
public class BeanWrapperImpl extends PropertyEditorRegistrySupport {

    /**
     * 目标对象
     */
    private Object wrappedObject;

    private Class<?> clz;

    /**
     * 参数值
     */
    private PropertyValues pvs;

    public BeanWrapperImpl(Object object) {
        //不同数据类型的参数转换器editor
        registerDefaultEditors();
        this.wrappedObject = object;
        this.clz = object.getClass();
    }

    public void setBeanInstance(Object object) {
        this.wrappedObject = object;
    }

    public Object getBeanInstance() {
        return wrappedObject;
    }

    /**
     * 绑定参数值
     *
     * @param pvs
     */
    public void setPropertyValues(PropertyValues pvs) {
        this.pvs = pvs;
        for (PropertyValue pv : this.pvs.getPropertyValues()) {
            setPropertyValue(pv);
        }
    }

    /**
     * 绑定具体某个参数
     *
     * @param pv
     */
    private void setPropertyValue(PropertyValue pv) {
        //拿到参数处理器
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());
        //找到对该参数类型的editor,用户自定义优先
        PropertyEditor pe = this.getCustomEditor(propertyHandler.getPropertyClz());
        //如果没有自定义的，则使用默认的
        if (pe == null) {
            pe = this.getDefaultEditor(propertyHandler.getPropertyClz());
        }
        //设置参数值
        pe.setAsText((String) pv.getValue());
        propertyHandler.setValue(pe.getValue());
    }


    class BeanPropertyHandler {
        Method writeMethod = null;

        Method readMethod = null;

        Class<?> propertyClz = null;

        public Class<?> getPropertyClz() {
            return propertyClz;
        }

        public BeanPropertyHandler(String propertyName) {
            try {
                Field field = clz.getDeclaredField(propertyName);
                propertyClz = field.getType();
                this.writeMethod = clz.getDeclaredMethod("set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), propertyClz);
                this.readMethod = clz.getDeclaredMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Object getValue() {
            Object result = null;
            writeMethod.setAccessible(true);
            try {
                result = readMethod.invoke(wrappedObject);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return result;

        }

        public void setValue(Object value) {
            writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
