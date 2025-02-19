package com.aix.annotation;

import com.aix.handler.ValueConvertHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonField {

    String name() default "";

    Class<? extends ValueConvertHandler> valueConvert() default ValueConvertHandler.class;

}
