package com.xjtu.dlc.autoqa.weixin.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import me.chanjar.weixin.mp.util.json.WxMpGsonBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class JsonUtils {
    public static String toJson(Object obj) {
        return WxMpGsonBuilder.create().toJson(obj);
    }

    public static void print(Object o) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(o));
    }

    public static String toJsonBeautiful(Object o) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(o);
    }

    public static <T> List<T> toList(String json, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    public static <T> LinkedList<T> jsonToLinkedList(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        LinkedList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        LinkedList<T> linkedList = new LinkedList<>();
        for (JsonObject jsonObject : jsonObjects) {
            linkedList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return linkedList;
    }

    public static void main(String[] args){
    }
}
