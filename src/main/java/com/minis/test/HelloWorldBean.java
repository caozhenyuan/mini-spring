package com.minis.test;

import com.minis.web.RequestMapping;

/**
 * @author 曹振远
 * @date 2023/03/27
 **/
public class HelloWorldBean {

    @RequestMapping("/test")
    public String doGet() {
        return "hello world get!";
    }

    public String doPost() {
        return "hello world!";
    }
}
