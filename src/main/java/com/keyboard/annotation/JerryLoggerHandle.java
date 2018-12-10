package com.keyboard.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author 2B键盘
 * @Date 2018/12/11 1:16
 * @Description 自己写的一个简单的日志记录
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JerryLoggerHandle {

    /**
     * 文件路径
     * @return
     */
    String value() default "";

}
