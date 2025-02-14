package com.aix;

import com.aix.parser.TypeRef;

import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        String json = """
           [
               {
                    "id": 1,
                    "key": "GZ",
                    "val": "T_1001",
                    "children": [
                        {
                            "id": 3,
                            "key": "GZ_THQ",
                            "val": "T_1003",
                        }
                    ]
               },
               {
                    "id": 2,
                    "key": "SZ",
                    "val": "T_1002",
               }
           ]
        """;
        List<Map<String,Object>> value1 = JsonPlus.parse(json, new TypeRef<>() {});
        List<Entity> value2 = JsonPlus.parse(json, new TypeRef<>() {});
        System.out.println(JsonPlus.toJson(value1));
        System.out.println(JsonPlus.toJson(value2));
    }

}
