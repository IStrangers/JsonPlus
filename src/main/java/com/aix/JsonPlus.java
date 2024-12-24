package com.aix;

import com.aix.parser.DefaultParser;
import com.aix.parser.Parser;
import com.aix.parser.ast.Expression;

public class JsonPlus {

    public static <T> T parse(String json, Class<T> clazz) {
        if (json == null || (!json.startsWith("{") && !json.startsWith("["))) {
            return null;
        }
        Parser parser = new DefaultParser(json);
        Expression expression = parser.parse();
        return null;
    }

}
