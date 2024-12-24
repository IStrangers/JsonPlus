package com.aix.parser.ast;

import java.util.Map;

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

}
