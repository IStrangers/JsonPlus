package com.aix;

public class Test {

    public static void main(String[] args) {
        String json = """
          {
            "name": "项目信息-项目",
            "code": "xmxx-xm",
            "type": "json",
            "status": 1,
            "useEnv": "web",
            "webDataUrl": "https://127.0.0.1/",
            "method": "post",
            "params": {
              "pageNum": 1,
              "pageSize": 1000
            },
            "jsonDataPath": "dataTable.rows",
            "dataHandleRules": [
              {
                "type": "Filter",
                "dataFilter": "[1154,1306].includes(id)"
              },
              {
                "type": "RequiredFields",
                "requiredFields": "id,name"
              }
            ],
            "mainProgram": {
              "runCode": "default",
              "args": {
                "usePaginationQuery": true,
                "paginationSize": 1000
              }
            },
            "tokenHandler": {
              "runCode": "default",
              "username": "",
              "password": ""
            },
            "outData": {
              "type": "StorageTable",
              "tableName": "project",
              "triggerUpdateFields": "id"
            },
            "schedule": {
              "type": "Cron",
              "timer": "0 5 0 * * ?"
            },
            "nextDataCrawlerCode": "xmxx-gzw"
          }
        """;
        Object value = JsonPlus.parse(json, null);
        System.out.println(value);
    }

}
