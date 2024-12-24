package com.aix.parser.ast;

public abstract class AbstractExpression implements Expression {

    abstract public String toJson(int deepLevel);

    @Override
    public String toString() {
        return toJson();
    }

    @Override
    public String toJson() {
        return toJson(0);
    }

}
