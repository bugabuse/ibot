/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package com.farm.server.core.util;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

public class JsonUtils {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static <T> T fromFile(String file, Class<T> type) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file));){
            Gson gson = new Gson();
            Object object = gson.fromJson((Reader)reader, type);
            return (T)object;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static <T> T fromFile(String file, Type type) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file));){
            Gson gson = new Gson();
            Object object = gson.fromJson((Reader)reader, type);
            return (T)object;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void toFile(String file, Object object) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));){
            Gson gson = new Gson();
            gson.toJson(object, (Appendable)writer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

