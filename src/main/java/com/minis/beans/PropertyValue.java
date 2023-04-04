package com.minis.beans;

/**
 * @author 曹振远
 * @date 2023/03/15
 **/
public class PropertyValue {

    private final String type;
    private final String name;
    private final Object value;

    private final boolean isRef;

    public PropertyValue(String name, Object value) {
        this("", name, value, false);
    }

    public PropertyValue(String type, String name, Object value, boolean isRef) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isRef = isRef;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public boolean getIsRef() {
        return isRef;
    }
}
