/*
 * Decompiled with CFR 0.148.
 */
package com.farm.server.core.util;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

public class FileUtils {
    public static String getBaseDirectory() {
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        return dir.toString() + File.separator;
    }

    public static File getFile(String fileName) {
        return Paths.get(fileName).toFile();
    }
}

