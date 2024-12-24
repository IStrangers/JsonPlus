package com.aix.parser.ast;

public class NumberExpression extends StringExpression {

    public NumberExpression(String value) {
        super(value);
    }

    @Override
    public String toJson(int deepLevel) {
        return value;
    }

}
