package com.aix.parser;

public class TokenObject {

    public TokenObject(Token token, String value) {
        this.token = token;
        this.value = value;
    }

    private final Token token;
    private final String value;

    public Token getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

}
