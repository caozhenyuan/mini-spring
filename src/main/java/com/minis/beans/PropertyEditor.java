package com.minis.beans;

/**
 * @author czy
 * @date 2023/04/04
 **/
public interface PropertyEditor {

    void setAsText(String text);

    void setValue(Object value);

    Object getValue();

    String getAsText();
}
