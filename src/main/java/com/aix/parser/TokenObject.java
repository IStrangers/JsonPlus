package com.aix.parser;

public class TokenObject {

    public TokenObject(Token token, String value) {
        this.token = token;
        this.value = value;
    }

    private Token token;
    private String value;

    public Token getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

}
