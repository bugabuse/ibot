package com.farm.ibot.updater;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class UpdateFile {
    public String localLocation;
    public int hash = 0;
    private transient String baseLocation;

    public UpdateFile() {
    }

    public UpdateFile(String baseLocation, String localLocation) {
        this.localLocation = localLocation;
        this.baseLocation = baseLocation;
        this.load();
    }

    private void load() {
        try {
            this.hash = Arrays.hashCode(IOUtils.toByteArray(new FileInputStream(new File(this.baseLocation, this.localLocation))));
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public int getHash() {
        return this.hash;
    }
}
