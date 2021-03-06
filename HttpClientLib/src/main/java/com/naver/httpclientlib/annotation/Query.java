package com.naver.httpclientlib.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Query {
    String value();

    boolean encoded() default false;
    String encodeType() default Default.ENCODE;
}
