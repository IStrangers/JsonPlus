package com.aix.parser;

import com.aix.parser.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultParser implements Parser {

    private final String json;
    private int readIndex;
    private TokenObject tokenObject;

    private final Map<Character, Token> tokenMap = Map.of(
    '[', Token.LEFT_BRACKET,
    ']', Token.RIGHT_BRACKET,
    '{', Token.LEFT_BRACE,
    '}', Token.RIGHT_BRACE,
    ',', Token.COMMA,
    ':', Token.COLON
    );

    public DefaultParser(String json) {
        this.json = json;
        this.readIndex = 0;
    }

    public Token getToken() {
        return tokenObject.getToken();
    }

    public String getTokenValue() {
        return tokenObject.getValue();
    }

    @Override
    public Expression parse() {
        nextToken();
        return parseExpression();
    }

    public char readChar() {
        if (readIndex < json.length()) {
            return json.charAt(readIndex++);
        }
        return (char) -1;
    }

    public void nextToken() {
        char c = readChar();

        if (tokenMap.containsKey(c)) {
            Token token = tokenMap.get(c);
            tokenObject = new TokenObject(token, token.getValue());
            return;
        }

        if ('"' == c) {
            tokenObject = new TokenObject(Token.STRING, scanString());
            return;
        }

        if (Character.isDigit(c)) {
            tokenObject = new TokenObject(Token.NUMBER, scanNumber());
            return;
        }

        if (c == (char) -1) {
            tokenObject = new TokenObject(null,null);
            return;
        }

        throw new IllegalArgumentException("Unexpected character: " + c);
    }

    public void expect(Token token) {
        expect(token,true);
    }
    public void expect(Token token, boolean throwError) {
        if (token != tokenObject.getToken()) {
            if (throwError) throw new IllegalArgumentException("Unexpected token: " + token);
            return;
        }
        nextToken();
    }

    public void expectOrSkip(Token token) {
        expect(token,false);
    }

    public String scanString() {
        StringBuilder sb = new StringBuilder();
        char c;
        while ((c = readChar()) != '"') {
            if (c == '\\') {
                c = readChar();
                switch (c) {
                    case 'b': sb.append('\b'); break;
                    case 't': sb.append('\t'); break;
                    case 'n': sb.append('\n'); break;
                    case 'f': sb.append('\f'); break;
                    case 'r': sb.append('\r'); break;
                    case '\"': sb.append('\"'); break;
                    case '\'': sb.append('\''); break;
                    case '\\': sb.append('\\'); break;
                    case 'u':
                        sb.append(readUnicodeEscape());
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid escape character: " + c);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public char readUnicodeEscape() {
        int codePoint = 0;
        for (int i = 0; i < 4; i++) {
            char c = readChar();
            if (!Character.isDigit(c) && !(c >= 'a' && c <= 'f') && !(c >= 'A' && c <= 'F')) {
                throw new NumberFormatException("Invalid Unicode escape sequence.");
            }
            codePoint = codePoint * 16 + Character.digit(c, 16);
        }
        return (char) codePoint;
    }

    public String scanNumber() {
        int start = readIndex - 1;
        boolean isNegative = json.charAt(start) == '-';

        while (readIndex < json.length() &&
            (
                Character.isDigit(json.charAt(readIndex)) ||
                json.charAt(readIndex) == '.' ||
                json.charAt(readIndex) == 'e' ||
                json.charAt(readIndex) == 'E' ||
                (json.charAt(readIndex) == '+' || json.charAt(readIndex) == '-') &&
                (json.charAt(readIndex - 1) == 'e' || json.charAt(readIndex - 1) == 'E')
            )
        ) {
            readIndex++;
        }
        String numberStr = json.substring(start, readIndex);
        return isNegative ? '-' + numberStr : numberStr;
    }

    public Expression parseExpression() {
        Token token = getToken();
        return switch (token) {
            case LEFT_BRACKET -> parseArrayExpression();
            case LEFT_BRACE -> parseObjectExpression();
            case STRING -> parseStringExpression();
            case NUMBER -> parseNumberExpression();
            case BOOLEAN -> parseBooleanExpression();
            case NULL -> parseNullExpression();
            default -> throw new UnsupportedOperationException("Unsupported token: " + token);
        };
    }

    public ArrayExpression parseArrayExpression() {
        expect(Token.LEFT_BRACKET);
        List<Expression> members = new ArrayList<>();
        while (getToken() != Token.RIGHT_BRACKET) {
            members.add(parseExpression());
            expectOrSkip(Token.COMMA);
        }
        expect(Token.RIGHT_BRACKET);
        return new ArrayExpression(members);
    }

    public ObjectExpression parseObjectExpression() {
        expect(Token.LEFT_BRACE);
        Map<String,Expression> members = new HashMap<>();
        while (getToken() != Token.RIGHT_BRACE) {
            StringExpression key = parseStringExpression();
            Expression value = parseExpression();
            members.put(key.getValue(),value);
            expectOrSkip(Token.COMMA);
        }
        expect(Token.RIGHT_BRACE);
        return new ObjectExpression(members);
    }

    public StringExpression parseStringExpression() {
        StringExpression stringExpression = new StringExpression(getTokenValue());
        expect(Token.STRING);
        return stringExpression;
    }

    public NumberExpression parseNumberExpression() {
        NumberExpression numberExpression = new NumberExpression(getTokenValue());
        expect(Token.NUMBER);
        return numberExpression;
    }

    public BoolExpression parseBooleanExpression() {
        BoolExpression boolExpression = "true".equalsIgnoreCase(getTokenValue()) ? BoolExpression.CONST_TRUE : BoolExpression.CONST_FALSE;
        expect(Token.BOOLEAN);
        return boolExpression;
    }

    public NullExpression parseNullExpression() {
        expect(Token.NULL);
        return NullExpression.CONST_NULL;
    }

}
