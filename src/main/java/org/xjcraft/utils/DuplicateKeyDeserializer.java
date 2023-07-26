package org.xjcraft.utils;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

//自定义gson的错误处理，以便忽略出错的重复json
public class DuplicateKeyDeserializer implements JsonDeserializer<Map<String, String>> {

    @Override
    public Map<String, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<String, String> map = new HashMap<>();
        JsonObject jsonObject = json.getAsJsonObject();
        // 遍历 JSON 对象的键值对，将键和对应的值存储到一个 HashMap 中
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            map.put(key, entry.getValue().getAsString());
        }

        return map;
    }
}
