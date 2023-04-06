package com.minis.web.servlet.view;

import com.minis.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * @author czy
 * @date 2023/04/06
 **/
public class JstlView implements View {

    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=ISO-8859-1";
    private String contentType = DEFAULT_CONTENT_TYPE;
    private String requestContextAttribute;
    private String beanName;
    private String url;

    /**
     * 解析返回值set到Request请求中转发到页面中
     *
     * @param model
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Set<? extends Map.Entry<String, ?>> entries = model.entrySet();
        for (Map.Entry<String, ?> entry : entries) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        request.getRequestDispatcher(getUrl()).forward(request, response);
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    public void setRequestContextAttribute(String requestContextAttribute) {
        this.requestContextAttribute = requestContextAttribute;
    }

    public String getRequestContextAttribute() {
        return this.requestContextAttribute;
    }
}
