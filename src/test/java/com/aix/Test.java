package com.aix;

import com.aix.parser.TypeRef;
import com.aix.parser.ast.Expression;

import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        String json = """
           [
               {
                    "id": 1,
                    "key": "GZ",
                    "value": "广州",
                    "children": [
                        {
                            "id": 3,
                            "key": "GZ_THQ",
                            "value": "天河区",
                        }
                    ]
               },
               {
                    "id": 2,
                    "key": "SZ",
                    "value": "深圳",
               }
           ]
        """;
        List<Map<String,Object>> value1 = JsonPlus.parse(json, new TypeRef<>() {});
        List<Entity> value2 = JsonPlus.parse(json, new TypeRef<>() {});
        System.out.println(JsonPlus.toJson(value1));
        System.out.println(JsonPlus.toJson(value2));
    }

}
