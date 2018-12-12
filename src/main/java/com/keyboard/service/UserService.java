package com.keyboard.service;

import com.keyboard.annotation.JerryService;

/**
 * @Author 2B键盘
 * @Date 2018/12/12 16:19
 * @Description TODO
 */
@JerryService
public class UserService {

    public String login(String user,String pass){
        return "login====>"+user+","+pass;
    }

}
