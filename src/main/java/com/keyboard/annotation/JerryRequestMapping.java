package com.keyboard.annotation;

import java.lang.annotation.*;

/**
 * @Author 2B键盘
 * @Date 2018/12/11 1:16
 * @Description 请求地址映射注解
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JerryRequestMapping {

    String value() default "";

}
