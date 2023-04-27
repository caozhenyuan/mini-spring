package com.minis.test.controller;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.test.entity.AjaxResultVo;
import com.minis.test.entity.User;
import com.minis.test.service.UserService;
import com.minis.web.bind.annotation.RequestMapping;
import com.minis.web.bind.annotation.ResponseBody;

/**
 * @author czy
 * @date 2023/04/26
 **/
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserById")
    @ResponseBody
    public AjaxResultVo<User> getUserById() {
        return new AjaxResultVo<>(userService.getUserById(1));
    }

    @RequestMapping("/update")
    @ResponseBody
    public AjaxResultVo<Integer> update() {
        return new AjaxResultVo<>(userService.updateUserBirthdayById(1));
    }
}
