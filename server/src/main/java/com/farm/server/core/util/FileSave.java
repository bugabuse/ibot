/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 */
package com.farm.server.core.util;

import com.google.gson.Gson;
import com.farm.server.rest.AccountsController;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileSave {
    public static void updateContent(File file, String str) {
        FileSave.writeFile(new File(file.getAbsolutePath() + "_backup"), str);
        FileSave.writeFile(file, str);
    }

    public static void serialize(File file, Object object) {
        try {
            BufferedWriter writer1 = new BufferedWriter(new FileWriter(new File(file.getAbsolutePath() + "_backup")));
            AccountsController.GSON.toJson(object, (Appendable)writer1);
            writer1.close();
            BufferedWriter writer2 = new BufferedWriter(new FileWriter(new File(file.getAbsolutePath())));
            AccountsController.GSON.toJson(object, (Appendable)writer2);
            writer2.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeFile(File file, String str) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(str);
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

