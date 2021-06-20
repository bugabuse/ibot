// Decompiled with: Procyon 0.5.36
package com.farm.ibot.updater;

import com.farm.ibot.Main;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.WebUtils;
import com.farm.ibot.init.Settings;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Updater {
    public static void createUpdateInfo() throws IOException {
        final UpdatePack currentPack = UpdateFiles.createUpdatePack(new File(Settings.BOT_DATA_PATH));
        final FileWriter writer = new FileWriter(new File(Settings.BOT_DATA_PATH) + File.separator + "updates.json");
        new Gson().toJson((Object) currentPack, (Appendable) writer);
        IOUtils.closeQuietly(writer);
    }

    public static ArrayList<UpdateFile> getFilesToUpdate() throws IOException {
        final UpdatePack currentPack = UpdateFiles.createUpdatePack(new File(Settings.BOT_DATA_PATH));
        currentPack.remoteDirectory = "http://api.hax0r.farm:8080/download/";
        final UpdatePack remotePack = (UpdatePack) new Gson().fromJson(WebUtils.downloadString("http://api.hax0r.farm:8080/download//updates.json"), (Class) UpdatePack.class);
        final ArrayList<UpdateFile> files = currentPack.getOutdatedFiles(remotePack).files;
        files.removeIf(file -> file.localLocation.contains("updates.json"));
        files.removeIf(file -> file.localLocation.contains("settings"));
        files.removeIf(file -> Main.bots.size() > 0 && file.localLocation.toLowerCase().contains("osrsbot.jar"));
        return files;
    }

    public static boolean updateFiles() throws IOException {
        final UpdatePack currentPack = UpdateFiles.createUpdatePack(new File(Settings.BOT_DATA_PATH));
        currentPack.remoteDirectory = "http://api.hax0r.farm:8080/download/";
        final ArrayList<UpdateFile> files = getFilesToUpdate();
        files.removeIf(f -> f.localLocation.contains("settings"));
        Debug.log("Updating " + files.size() + " files.");
        for (final UpdateFile file : files) {
            downloadFile(currentPack, file);
        }

        return true;
    }

    private static void downloadFile(final UpdatePack currentPack, final UpdateFile updateFile) throws IOException {
        final String url = (currentPack.remoteDirectory + updateFile.localLocation).replace("\\", "/");

        final File file = new File(currentPack.localDirectory.replace("\\", File.separator), updateFile.localLocation.replace("\\", File.separator));
        FileUtils.copyURLToFile(new URL(url), file);
    }

    public static void pushUpdatesToServer() throws IOException, InterruptedException {

        final UpdatePack currentPack = UpdateFiles.createUpdatePack(new File(Settings.BOT_DATA_PATH));
        final UpdateFile updateInfoFile = new ArrayList<UpdateFile>(currentPack.files).stream()
                .filter(file -> file.localLocation.contains("updates.json"))
                .findAny()
                .orElse(null);
        currentPack.files.remove(updateInfoFile);
        currentPack.files.removeIf(f -> f.localLocation.contains("settings"));
        final UpdatePack remotePack = getRemoteUpdatePack();
        final UpdatePack outdated = remotePack.getOutdatedFiles(currentPack);
        Debug.log("Pushing " + outdated.files.size() + " updates.");
        for (final UpdateFile updateFile : outdated.files) {
            final File file2 = new File(Settings.BOT_DATA_PATH, updateFile.localLocation);
            FileUpload.upload(file2, updateFile.localLocation);
        }
        if (outdated.files.size() > 0) {
            final File file3 = new File(Settings.BOT_DATA_PATH, updateInfoFile.localLocation);
            FileUpload.upload(file3, updateInfoFile.localLocation);
        }

    }

    private static UpdatePack getRemoteUpdatePack() {
        final UpdatePack pack = new UpdatePack(new File(Settings.BOT_DATA_PATH).getAbsolutePath());
        final String str = WebUtils.downloadString("http://api.hax0r.farm:8080/versioncheck.php");
        for (final String line : str.split("<br>")) {
            final String fileName = line.split(":")[0];
            if (line.split(":").length >= 2) {
                final int hashCode = Integer.parseInt(line.split(":")[1]);
                final UpdateFile file = new UpdateFile();
                file.localLocation = fileName;
                file.hash = hashCode;
                pack.addFile(file);
            }
        }
        return pack;
    }
}
