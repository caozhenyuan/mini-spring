package com.minis.web;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author czy
 * @date 2023/04/06
 **/
public class DefaultHttpMessageConverter implements HttpMessageConverter {

    private String defaultContentType = "text/json;charset=UTF-8";

    private String defaultCharacterEncoding = "UTF-8";

    private ObjectMapper objectMapper;

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 把 Object 转成 JSON 串
     *
     * @param obj
     * @param response
     * @throws IOException
     */
    @Override

    public void write(Object obj, HttpServletResponse response) throws IOException {
        response.setContentType(defaultContentType);
        response.setCharacterEncoding(defaultCharacterEncoding);
        writeInternal(obj, response);
        response.flushBuffer();
    }

    /**
     * 把对象转换为Json放入Response返回
     *
     * @param obj
     * @param response
     */
    private void writeInternal(Object obj, HttpServletResponse response) {
        String json;
        try {
            json = this.objectMapper.writeValueAsString(obj);
            PrintWriter writer = response.getWriter();
            writer.write(json);
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
