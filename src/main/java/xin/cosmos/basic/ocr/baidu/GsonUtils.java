/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package xin.cosmos.basic.ocr.baidu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Json工具类.
 */
public class GsonUtils {
    private final static Gson GSON = new GsonBuilder().create();

    public static String toJson(Object value) {
        return GSON.toJson(value);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonParseException {
        return GSON.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) throws JsonParseException {
        return (T) GSON.fromJson(json, typeOfT);
    }
}
