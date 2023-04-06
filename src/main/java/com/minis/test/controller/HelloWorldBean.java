package com.minis.test.controller;

import com.minis.test.entity.User;
import com.minis.web.bind.annotation.RequestMapping;
import com.minis.web.bind.annotation.ResponseBody;

import java.util.Date;

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

    @RequestMapping("/test7")
    @ResponseBody
    public User doTest7(User user) {
        user.setName(user.getName() + "---");
        user.setBirthday(new Date());
        return user;
    }
}
