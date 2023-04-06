package com.minis.web;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author czy
 * @date 2023/04/06
 **/
public interface HttpMessageConverter {

    /**
     * 让 controller 返回给前端的字符流数据可以进行格式转换
     *
     * @param obj
     * @param response
     * @throws IOException
     */
    void write(Object obj, HttpServletResponse response) throws IOException;
}
