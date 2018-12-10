package com.keyboard.controller;

import com.keyboard.annotation.JerryAutowired;
import com.keyboard.annotation.JerryController;
import com.keyboard.annotation.JerryRequestMapping;
import com.keyboard.annotation.JerryRequestParam;
import com.keyboard.service.TomService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author 2B键盘
 * @Date 2018/12/11 1:33
 * @Description TODO
 */
@JerryController
@JerryRequestMapping("/jerry")
public class TomController {

    @JerryAutowired("TomServiceInterface")
    private TomService tomService;

    @JerryRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response,
                      @JerryRequestParam("name") String name,
                      @JerryRequestParam("age") String age) {
        //返回数据
        try {
            PrintWriter ps = response.getWriter();
            String result = tomService.query(name, age);
            ps.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
