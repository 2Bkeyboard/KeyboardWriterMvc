package com.keyboard.annotation;

import java.lang.annotation.*;

/**
 * @Author 2B键盘 请求地址映射注解
 * @Date 2018/12/11 1:16
 * Description TODO
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JerryRequestMapping {

    String value() default "";

}
