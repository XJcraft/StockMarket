package org.xjcraft.utils;

import com.google.gson.Gson;

public class JSON {
    private static final Gson gson = new Gson();

    public static String toJSONString(Object object) {
        return gson.toJson(object);
    }
}
