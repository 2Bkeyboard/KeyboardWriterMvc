package com.keyboard.util;


import com.keyboard.annotation.JerryLoggerHandle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Logger {

    //class对象
    private Class classType;

    //保存文件信息,用于重新创建文件
    public Map<String, String> fileInfo;

    //堆栈轨迹
    StackTraceElement[] stackTrace = null;
    StackTraceElement e = null;

    //调用类信息
    protected String className;
    protected String fileName;
    protected String methodName;
    protected int lineNumber;
    protected static String info;

    public Logger(Class classType) {
        this.classType = classType;
        stackTrace = Thread.currentThread().getStackTrace();
        e = stackTrace[2];
        //获取信息,在类初始化时获取
        className = e.getClassName();
        fileName = e.getFileName();
        methodName = e.getMethodName();
    }

    public void info(Object msg) {
        //获取当前线程轨迹
        stackTrace = Thread.currentThread().getStackTrace();
        e = stackTrace[2];
        //获取调用行数
        lineNumber = e.getLineNumber();
        //输出
        info = getDate() + "|" + className + ":[" + e.getMethodName() + "][" + lineNumber + "] INFO - " + msg;
        System.out.println(info);
        JerryLoggerHandleAnnotation(info);
    }

    public void error(Exception e) {
        //获取当前线程轨迹
        stackTrace = Thread.currentThread().getStackTrace();
        this.e = stackTrace[2];
        //获取调用行数
        lineNumber = this.e.getLineNumber();
        //输出
        info = getDate() + "|" + className + ":[" + this.e.getMethodName() + "][" + lineNumber + "] ERROR - ";
        //获取报错信息
        info = info + e.getClass().getName() + ":" + e.getMessage();
        System.err.println(info);
        JerryLoggerHandleAnnotation(info);
    }

    public void error(String msg) {
        //获取当前线程轨迹
        stackTrace = Thread.currentThread().getStackTrace();
        this.e = stackTrace[2];
        //获取调用行数
        lineNumber = this.e.getLineNumber();
        //输出
        info = getDate() + "|" + className + ":[" + this.e.getMethodName() + "][" + lineNumber + "] ERROR - " + msg;
        //获取报错信息
        System.err.println(info);
        JerryLoggerHandleAnnotation(info);
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 解析注解
     */
    public void JerryLoggerHandleAnnotation(String content) {
        Class<?> obj = this.classType;
        try {
            Field field = obj.getDeclaredField("log");
            //判断当前成员上是否有注解
            boolean isPres = field.isAnnotationPresent(JerryLoggerHandle.class);
            if (isPres != false) {
                JerryLoggerHandle JerryLoggerHandle = field.getAnnotation(JerryLoggerHandle.class);
                //日志文件处理器
                fileFactory(JerryLoggerHandle.value(), content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fileFactory(String path, String content) {
        File file = new File(path);
        FileWriter fileWriter = null;
        //如果没有文件就创建In
        if (file.exists()) {
            file.mkdir();
        }
        try {
            //true为是否追加内容
            fileWriter = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        //获取文件大小 file.length / 1024 得到的大小是以KB为单位的
        long fileSize = file.length() / 1024;
        //写入内容 判断文件是否有大小指定
        printWriter.append(content + "\n");
        printWriter.flush();
        //关闭流
        try {
            fileWriter.close();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
