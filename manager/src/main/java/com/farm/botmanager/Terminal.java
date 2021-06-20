package com.farm.botmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Terminal {
    public static Process execute(String path, String commands) {
        System.out.println("Starting process!");
        ArrayList<String> args = new ArrayList<String>(Arrays.asList("java", "-Xbootclasspath/p:" + Settings.XBOOT_JAR_FILE.getAbsolutePath(), Main.START_MEMORY, "-Xms1024m", "-jar", path));
        String[] arr = commands.split(" ");
        args.addAll(Arrays.asList(arr));
        ProcessBuilder pb = new ProcessBuilder(args);
        System.out.println("Starting: " + args.toString());
        try {
            return pb.start();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String execute(String commands) {
        ArrayList<String> args = new ArrayList<String>();
        String[] arr = commands.split(" ");
        Arrays.stream(arr).forEach(args::add);
        ProcessBuilder pb = new ProcessBuilder(args);
        try {
            int i;
            StringBuilder builder = new StringBuilder();
            Process proc = pb.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            while ((i = stdInput.read()) != -1) {
                builder.append((char) i);
            }
            while ((i = stdError.read()) != -1) {
                builder.append((char) i);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void killProcess(String pid) {
        if (Settings.isWindows) {
            Terminal.execute("taskkill /pid " + pid + " /f");
        } else {
            Terminal.execute("kill -9 " + pid + "");
        }
    }
}

