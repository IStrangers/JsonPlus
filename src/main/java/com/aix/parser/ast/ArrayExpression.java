package com.aix.parser.ast;

import java.util.List;

public class ArrayExpression extends AbstractExpression {

    public ArrayExpression(List<Expression> members) {
        this.members = members;
    }

    private List<Expression> members;

    public List<Expression> getMembers() {
        return members;
    }

    public Expression getMember(int i) {
        return members.get(i);
    }

}
