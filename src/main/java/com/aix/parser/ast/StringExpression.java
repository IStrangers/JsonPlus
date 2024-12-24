package com.aix.parser.ast;

public class StringExpression extends AbstractExpression {

    private String value;

    public StringExpression(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
