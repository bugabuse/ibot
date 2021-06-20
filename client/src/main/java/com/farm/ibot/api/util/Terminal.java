package com.farm.ibot.api.util;

import com.farm.ibot.init.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;

public class Terminal {
    private static long pid = -1L;

    public static String executeOne(String command) {
        try {
            Process exec = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            return stdInput.readLine();
        } catch (IOException var3) {
            var3.printStackTrace();
            return "";
        }
    }

    public static String execute(String commands) {
        String[] arr = commands.split(" ");
        ArrayList<String> args = new ArrayList(Arrays.asList(arr));
        ProcessBuilder pb = new ProcessBuilder(args);

        try {
            StringBuilder builder = new StringBuilder();
            Process proc = pb.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            int i;
            while ((i = stdInput.read()) != -1) {
                builder.append((char) i);
            }

            while ((i = stdError.read()) != -1) {
                builder.append((char) i);
            }

            return builder.toString();
        } catch (IOException var9) {
            var9.printStackTrace();
            return "";
        }
    }

    public static void killCurrentProcess() {
        if (Settings.isWindows) {
            execute("taskkill /pid " + getProcessId() + " /f");
        } else {
            execute("kill " + getProcessId() + "");
        }

    }

    public static long getProcessId() {
        if (Terminal.pid == -1L) {
            String vmName = ManagementFactory.getRuntimeMXBean().getName();
            int p = vmName.indexOf("@");
            String pid = vmName.substring(0, p);
            Terminal.pid = (long) Integer.parseInt(pid);
        }

        return Terminal.pid;
    }
}
