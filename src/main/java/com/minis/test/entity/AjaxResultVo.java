package com.minis.test.entity;

import java.io.Serializable;

public class AjaxResultVo<T> implements Serializable {

    private static final long serialVersionUID = -836653662871514751L;

    public static final int SUCCESS = 200; // 成功

    public static final int CHECK_FAIL = 400; // 参数错误

    public static final int UNKNOWN_EXCEPTION = 500;

    /**
     * 是否成功
     */

    private Boolean success = false;

    /**
     * 状态码
     */
    private Integer code = SUCCESS;

    /**
     * 信息
     */
    private String msg = "success";

    /**
     * 数据
     */
    private T data;

    public AjaxResultVo() {
    }

    public AjaxResultVo(T data) {
        this.data = data;
        this.success = true;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
