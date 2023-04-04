package com.minis.beans;

import com.minis.util.NumberUtils;
import com.minis.util.StringUtils;

import java.text.NumberFormat;

/**
 * @author czy
 * @date 2023/04/04
 * @Deprecated 处理Number类型
 **/
public class CustomNumberEditor implements PropertyEditor {

    /**
     * 数据类型
     */
    private Class<? extends Number> numberClass;

    /**
     * 指定格式
     */
    private NumberFormat numberFormat;

    private boolean allowEmpty;

    private Object value;

    public CustomNumberEditor(Class<? extends Number> numberClass,
                              boolean allowEmpty) throws IllegalArgumentException {
        this(numberClass, null, allowEmpty);
    }

    public CustomNumberEditor(Class<? extends Number> numberClass,
                              NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowEmpty = allowEmpty;
    }

    /**
     * 将一个字符串转换为number赋值
     *
     * @param text
     */
    @Override
    public void setAsText(String text) {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            setValue(null);
        } else if (null != this.numberFormat) {
            //给定格式
            setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
        } else {
            setValue(NumberUtils.parseNumber(text, this.numberClass));
        }
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof Number) {
            this.value = NumberUtils.convertNumberToTargetClass((Number) value, this.numberClass);
        } else {
            this.value = value;
        }
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    /**
     * 将number表示成格式化串
     *
     * @return
     */
    @Override
    public String getAsText() {
        Object value = this.value;
        if (null == value) {
            return "";
        }
        //使用NumberFormat来呈现值
        if (null != this.numberFormat) {
            return this.numberFormat.format(value);
        } else {
            //使用toString方法来呈现值
            return value.toString();
        }
    }
}
