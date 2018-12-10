package com.keyboard.annotation;

import java.lang.annotation.*;

/**
 * @Author 2B键盘 获取参数注解
 * @Date 2018/12/11 1:21
 * Description TODO
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JerrayRequestParam {

    String value() default "";

}
