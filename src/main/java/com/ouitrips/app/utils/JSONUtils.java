package com.ouitrips.app.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class JSONUtils {

    public static List<String> jsonToList(String jsonString){
        List<String> stringList = new ArrayList<>();
        int startIndex = jsonString.indexOf("[") + 1;
        int endIndex = jsonString.lastIndexOf("]");
        String itemsPart = jsonString.substring(startIndex, endIndex);

        String[] items = itemsPart.split(",");
        for (String item : items) {
            String trimmedItem = item.trim();
            if (trimmedItem.startsWith("\"") && trimmedItem.endsWith("\"")) {
                stringList.add(trimmedItem.substring(1, trimmedItem.length() - 1));
            }
        }
        return stringList;
    }

    public static String listToJson(List<String> listRoles){
        StringBuilder jsonStringBuilder = new StringBuilder();
        jsonStringBuilder.append("[");
        for (int i = 0; i < listRoles.size(); i++) {
            jsonStringBuilder.append("\"").append(listRoles.get(i)).append("\"");
            if (i < listRoles.size() - 1) {
                jsonStringBuilder.append(",");
            }
        }
        jsonStringBuilder.append("]");
        return jsonStringBuilder.toString();
    }

}
