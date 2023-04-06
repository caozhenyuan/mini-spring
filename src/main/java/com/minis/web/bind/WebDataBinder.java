package com.minis.web.bind;

import com.minis.beans.PropertyEditor;
import com.minis.beans.PropertyValues;
import com.minis.util.WebUtils;
import com.minis.web.BeanWrapperImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author czy
 * @date 2023/04/04
 * @Deprecated 用于将 Request 请求内的字符串参数转换成不同类型的参数，来进行适配
 **/
public class WebDataBinder {

    private Object target;

    private Class<?> clz;

    private String objectName;

    public WebDataBinder(Object target) {
        this(target, "");
    }

    public WebDataBinder(Object target, String targetName) {
        this.target = target;
        this.objectName = targetName;
        this.clz = this.target.getClass();
    }

    /**
     * 核心绑定方法，将request里面的参数值绑定到目标对象的属性上
     *
     * @param request
     */
    public void bind(HttpServletRequest request) {
        //1.把 Request 里的参数解析成 PropertyValues
        PropertyValues mpvs = assignParameters(request);
        //2.把 Request 里的参数值添加到绑定参数中
        addBindValues(mpvs, request);
        //3.把两者绑定在一起
        doBind(mpvs);
    }

    private void doBind(PropertyValues mpvs) {
        applyPropertyValues(mpvs);
    }

    /**
     * 时间将参数值与对象属性进行绑定的方法
     *
     * @param mpvs
     */
    protected void applyPropertyValues(PropertyValues mpvs) {
        getPropertyAccessor().setPropertyValues(mpvs);
    }

    /**
     * 设置属性值的工具
     *
     * @return
     */
    protected BeanWrapperImpl getPropertyAccessor() {
        return new BeanWrapperImpl(this.target);
    }

    /**
     * 将Request参数解析成PropertyValues
     *
     * @param request
     * @return
     */
    private PropertyValues assignParameters(HttpServletRequest request) {
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);
    }

    /**
     * 注册自定义的 Editor
     *
     * @param requiredType
     * @param propertyEditor
     */
    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
    }

    protected void addBindValues(PropertyValues mpvs, HttpServletRequest request) {

    }
}
