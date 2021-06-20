package com.farm.ibot.updater;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;

public class UpdateFiles {
    public static UpdatePack createUpdatePack(File directory) throws IOException {
        UpdatePack pack = new UpdatePack(directory.getAbsolutePath());
        Files.walk(directory.toPath()).filter((x$0) -> {
            return Files.isRegularFile(x$0, new LinkOption[0]);
        }).forEach((f) -> {
            pack.addFile(new UpdateFile(directory.toString(), f.toString().substring(directory.toString().length())));
        });
        return pack;
    }
}
