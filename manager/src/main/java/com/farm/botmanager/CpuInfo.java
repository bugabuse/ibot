package com.farm.botmanager;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

public class CpuInfo {
    public static String getData() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor cpu = hal.getProcessor();
        System.out.println("CPU " + cpu.getName());
        System.out.println("Load " + cpu.getSystemCpuLoad());
        String load = (int) (cpu.getSystemCpuLoad() * 100.0) + "%";
        String name = cpu.getName();
        String str = String.format("[LOAD: %s], [%s]", load, name);
        System.out.format(str);
        return str;
    }
}

