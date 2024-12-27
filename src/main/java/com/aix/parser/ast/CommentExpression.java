package com.aix.parser.ast;

public class CommentExpression extends StringExpression {

    public CommentExpression(String value) {
        super(value);
    }

    @Override
    public String toJson(int deepLevel) {
        return value;
    }

}
