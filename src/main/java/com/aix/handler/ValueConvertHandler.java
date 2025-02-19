package com.aix.handler;

import com.aix.parser.ast.Expression;

import java.lang.reflect.Type;

public interface ValueConvertHandler {

    Object convert(Expression value, Type type);

}
