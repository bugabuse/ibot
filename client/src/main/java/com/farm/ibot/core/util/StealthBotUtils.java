package com.farm.ibot.core.util;

import com.farm.ibot.api.util.Random;

import java.util.HashMap;

public class StealthBotUtils {
    public static final StealthBotUtils.OsData fakeWindows10 = new StealthBotUtils.OsData("Windows 10", new String[]{"Oracle Corporation"}, new String[]{"1.8.0_241", "1.8.0_231", "1.8.0_211", "1.8.0_212", "1.8.0_221", "11.0.5", "1.8.0_171", "1.8.0_172", "1.7.0_172", "1.7.0_166"}, new String[]{"amd64", "x86_64"}, new String[]{"10.0"});
    public static final StealthBotUtils.OsData fakeWindows8 = new StealthBotUtils.OsData("Windows 8", new String[]{"Oracle Corporation"}, new String[]{"1.8.0_241", "1.8.0_231", "1.8.0_211", "1.8.0_212", "1.8.0_221", "11.0.5", "1.8.0_171", "1.8.0_172", "1.7.0_172", "1.7.0_166"}, new String[]{"amd64", "x86_64"}, new String[]{"6.3", "6.2"});
    public static final StealthBotUtils.OsData fakeWindows7 = new StealthBotUtils.OsData("Windows 7", new String[]{"Oracle Corporation"}, new String[]{"1.8.0_241", "1.8.0_231", "1.8.0_211", "1.8.0_212", "1.8.0_221", "11.0.5", "1.8.0_171", "1.8.0_172", "1.7.0_172", "1.7.0_166"}, new String[]{"amd64", "x86_64"}, new String[]{"6.1"});
    public static final StealthBotUtils.OsData[] fakeSystems;

    static {
        fakeSystems = new StealthBotUtils.OsData[]{fakeWindows10, fakeWindows7, fakeWindows8};
    }

    public static class OsData {
        public String systemName;
        public String[] javaVendors;
        public String[] javaVersions;
        public String[] osArchitectures;
        public String[] osVersions;

        public OsData(String systemName, String[] javaVendors, String[] javaVersions, String[] osArchitectures, String[] osVersions) {
            this.systemName = systemName;
            this.javaVendors = javaVendors;
            this.javaVersions = javaVersions;
            this.osArchitectures = osArchitectures;
            this.osVersions = osVersions;
        }
    }

    public static class SpoofedOperatingSystem {
        private final int processorCount;
        public HashMap<String, String> properties = new HashMap();

        public SpoofedOperatingSystem(String systemName, String javaVendor, String javaVersion, String osArchitecture, String osVersion, int processorCount) {
            this.properties.put("java.version", javaVersion);
            this.properties.put("java.vendor", javaVendor);
            this.properties.put("os.arch", osArchitecture);
            this.properties.put("os.version", osVersion);
            this.properties.put("os.name", systemName);
            this.processorCount = processorCount;
        }

        public static StealthBotUtils.SpoofedOperatingSystem createRandom() {
            StealthBotUtils.OsData data = StealthBotUtils.fakeSystems[Random.next(0, StealthBotUtils.fakeSystems.length)];
            return new StealthBotUtils.SpoofedOperatingSystem(data.systemName, data.javaVendors[Random.next(0, data.javaVendors.length)], data.javaVersions[Random.next(0, data.javaVersions.length)], data.osArchitectures[Random.next(0, data.osArchitectures.length)], data.osVersions[Random.next(0, data.osVersions.length)], 2 * Random.next(1, 3));
        }
    }
}
