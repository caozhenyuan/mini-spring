package com.minis.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author czy
 * @date 2023/04/04
 **/
public class WebUtils {

    public static Map<String, Object> getParametersStartingWith(HttpServletRequest request, String prefix) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();
        if (null == prefix) {
            prefix = "";
        }
        while (null != paramNames && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (prefix.isEmpty() || paramName.startsWith(prefix)) {
                String fixed = paramName.substring(prefix.length());
                String value = request.getParameter(paramName);
                params.put(fixed, value);
            }
        }
        return params;
    }
}
