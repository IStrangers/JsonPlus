package com.aix.parser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeRef<T> {

    public TypeRef() {
        Type superClass = this.getClass().getGenericSuperclass();
        this.type = ((ParameterizedType)superClass).getActualTypeArguments()[0];
    }

    private final Type type;

    public Type getType() {
        return type;
    }

}
