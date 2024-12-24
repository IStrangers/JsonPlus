package com.aix.parser.ast;

import java.util.Map;
import java.util.stream.Collectors;

public class ObjectExpression extends AbstractExpression{

    public ObjectExpression(Map<String, Expression> members) {
        this.members = members;
    }

    private Map<String,Expression> members;

    public Map<String, Expression> getMembers() {
        return members;
    }

    public Expression getMember(String member) {
        return members.get(member);
    }

    @Override
    public String toJson(int deepLevel) {
        return String.format("{\n%s\n%s}",members.entrySet().stream()
            .map(enter -> String.format("%s\"%s\": %s", "\t".repeat(deepLevel + 1), enter.getKey(), enter.getValue().toJson(deepLevel + 1)))
            .collect(Collectors.joining(",\n")),"\t".repeat(deepLevel)
        );
    }

}
