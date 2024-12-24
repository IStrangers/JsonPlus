package com.aix.parser.ast;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public String toJson(int deepLevel) {
        return String.format("[\n%s\n%s]",members.stream()
            .map(expression -> String.format("%s%s","\t".repeat(deepLevel + 1),expression.toJson(deepLevel + 1)))
            .collect(Collectors.joining(",\n")),"\t".repeat(deepLevel)
        );
    }

}
