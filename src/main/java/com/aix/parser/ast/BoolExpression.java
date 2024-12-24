package com.aix.parser.ast;

public class BoolExpression extends AbstractExpression {

    public static final BoolExpression CONST_TRUE = new BoolExpression(true);
    public static final BoolExpression CONST_FALSE = new BoolExpression(false);

    public BoolExpression(boolean value) {
        this.value = value;
    }

    private boolean value;

    public boolean isValue() {
        return value;
    }

}
