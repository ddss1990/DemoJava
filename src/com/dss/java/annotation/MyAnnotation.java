package com.dss.java.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: DSS
 * Date: 2018/9/8
 * Time: 23:09
 * Tag: Test Annotation
 */

@Retention(RetentionPolicy.RUNTIME) // 保留时间
// 可用来修饰的结构
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE})
// 格式是  ： @interface
public @interface MyAnnotation {
    String value();
}
