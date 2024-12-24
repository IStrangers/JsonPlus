package com.aix.parser.ast;

public class StringExpression extends AbstractExpression {

    protected String value;

    public StringExpression(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toJson(int deepLevel) {
        return String.format("\"%s\"", value);
    }

}
