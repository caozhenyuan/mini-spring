package com.minis.web;

import com.minis.test.entity.User;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * @author czy
 * @date 2023/04/06
 * @Deprecated ObjectMapper默认实现类
 **/
public class DefaultObjectMapper implements ObjectMapper {

    public String dateFormat = "yyyy-MM-dd";

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);

    public String decimalFormat = "#,##0.00";

    private DecimalFormat decimalFormatter = new DecimalFormat(decimalFormat);

    public DefaultObjectMapper() {

    }

    @Override
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public void setDecimalFormat(String decimalFormat) {
        this.decimalFormat = decimalFormat;
        this.decimalFormatter = new DecimalFormat(decimalFormat);
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getDecimalFormat() {
        return decimalFormat;
    }

    @Override
    public String writeValueAsString(Object obj) throws IllegalAccessException {
        String sJsonStr = "{";
        Class<?> clz = obj.getClass();

        Field[] fields = clz.getDeclaredFields();
        //对返回对象中的每一个属性进行格式转换
        for (Field field : fields) {
            //不序列化静态字段
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            String sField = "";
            Object value = null;
            Class<?> type = null;
            String name = field.getName();
            String strValue = "";
            field.setAccessible(true);
            value = field.get(obj);
            type = field.getType();
            //针对不同的数据类型进行格式转换
            if (value instanceof Date) {
                LocalDate localDate = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                strValue = localDate.format(this.dateTimeFormatter);
            } else if (value instanceof BigDecimal || value instanceof Double || value instanceof Float) {
                strValue = this.decimalFormatter.format(value);
            } else {
                strValue = value.toString();
            }
            //拼接json
            if ("{".equals(sJsonStr)) {
                sField = "\"" + name + "\":\"" + strValue + "\"";
            } else {
                sField = ",\"" + name + "\":\"" + strValue + "\"";
            }
            sJsonStr += sField;
        }
        sJsonStr += "}";
        return sJsonStr;
    }

}
