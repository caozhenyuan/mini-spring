package com.minis.beans;

/**
 * @author czy
 * @date 2023/04/04
 * @Deprecated 处理除了Number以外的其他类型
 **/
public class StringEditor implements PropertyEditor {

    private Class<String> strClass;

    private String strFormat;

    private boolean allowEmpty;

    private Object value;

    public StringEditor(Class<String> strClass,
                        boolean allowEmpty) throws IllegalArgumentException {
        this(strClass, "", allowEmpty);
    }

    public StringEditor(Class<String> strClass,
                        String strFormat, boolean allowEmpty) throws IllegalArgumentException {
        this.strClass = strClass;
        this.strFormat = strFormat;
        this.allowEmpty = allowEmpty;
    }

    @Override
    public void setAsText(String text) {
        setValue(text);
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String getAsText() {
        return value.toString();
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}
