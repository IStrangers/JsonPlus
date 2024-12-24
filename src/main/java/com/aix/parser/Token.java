package com.aix.parser;

public enum Token {

    COMMENT(""),
    MULTI_COMMENT(""),
    IDENTIFIER(""),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    LEFT_BRACKET("["),
    RIGHT_BRACKET("]"),
    DOT("."),
    COMMA(","),
    COLON(":"),
    NUMBER(""),
    STRING(""),
    BOOLEAN(""),
    NULL("");

    private final String value;

    Token(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
