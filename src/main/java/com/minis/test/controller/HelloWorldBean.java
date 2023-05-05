package com.minis.test.controller;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.entity.User;
import com.minis.test.service.IAction;
import com.minis.web.bind.annotation.RequestMapping;
import com.minis.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author 曹振远
 * @date 2023/03/27
 **/
public class HelloWorldBean {

    @Autowired
    private IAction action;

    @RequestMapping("/testAop")
    public void doTestAop(HttpServletRequest request, HttpServletResponse response) {
        action.doAction();
        String str = "test aop, hello world!";
        try {
            response.getWriter().write(str);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/test")
    @ResponseBody
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
