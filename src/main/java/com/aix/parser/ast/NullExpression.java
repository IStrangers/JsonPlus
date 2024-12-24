package com.aix.parser.ast;

public class NullExpression extends AbstractExpression {

    public static final NullExpression CONST_NULL = new NullExpression();

    @Override
    public String toJson(int deepLevel) {
        return "null";
    }

}
