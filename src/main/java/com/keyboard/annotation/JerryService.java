package com.keyboard.annotation;

import java.lang.annotation.*;

/**
 * @Author 2B键盘
 * @Date 2018/12/11 1:24
 * @Description 业务层注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JerryService {

    String value() default "";

}
