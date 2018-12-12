package com.keyboard.controller;

import com.keyboard.annotation.JerryAutowired;
import com.keyboard.annotation.JerryController;
import com.keyboard.annotation.JerryRequestMapping;
import com.keyboard.annotation.JerryRequestParam;
import com.keyboard.service.TomService;
import com.keyboard.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author 2B键盘
 * @Date 2018/12/12 16:17
 * @Description TODO
 */
@JerryController
@JerryRequestMapping("/user")
public class UserController {

    @JerryAutowired
    UserService userService;

    @JerryAutowired
    TomService service;

    @JerryRequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response,
                      @JerryRequestParam("user") String user,
                      @JerryRequestParam("pass") String pass){
        //返回数据
        try {
            PrintWriter ps = response.getWriter();
            String result = userService.login(user,pass);
            result += service.query(user,pass);
            ps.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
