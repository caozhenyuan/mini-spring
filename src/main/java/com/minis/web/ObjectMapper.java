package com.minis.web;

/**
 * @author czy
 * @date 2023/04/06
 **/
public interface ObjectMapper {

    /**
     * 将对象转换为JSON字符串时使用的日期格式进行设置
     *
     * @param dateFormat 日期格式(例：yyyy-MM-dd)
     */
    void setDateFormat(String dateFormat);

    /**
     * 将对象转换为JSON字符串时使用的数字格式进行设置
     *
     * @param decimalFormat 数字格式(例：#.## 以下代码将数字格式化为最多两位小数的字符串)
     */
    void setDecimalFormat(String decimalFormat);

    /**
     * 对象转JSON
     *
     * @param obj
     * @return
     */
    String writeValueAsString(Object obj) throws IllegalAccessException;
}
