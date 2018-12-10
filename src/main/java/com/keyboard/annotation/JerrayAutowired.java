package com.keyboard.annotation;

import java.lang.annotation.*;

/**
 * @Author 2B键盘 依赖注入注解
 * @Date 2018/12/11 1:18
 * Description TODO
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JerrayAutowired {
}
