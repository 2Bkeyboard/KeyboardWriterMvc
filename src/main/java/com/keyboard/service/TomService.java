package com.keyboard.service;

import com.keyboard.annotation.JerryService;
import com.keyboard.service.interfaces.TomServiceInterface;

/**
 * @Author 2B键盘
 * @Date 2018/12/11 1:29
 * @Description TODO
 */
@JerryService("tomService")
public class TomService implements TomServiceInterface {

    public String query(String name, String age) {
        return "name=="+name+",age=="+age;
    }

}
