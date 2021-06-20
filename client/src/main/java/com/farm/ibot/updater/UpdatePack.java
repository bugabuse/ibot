package com.farm.ibot.updater;

import java.util.ArrayList;
import java.util.Iterator;

public class UpdatePack {
    public String localDirectory;
    public String remoteDirectory;
    public ArrayList<UpdateFile> files = new ArrayList();

    public UpdatePack(String baseDirectory, ArrayList<UpdateFile> files) {
        this.localDirectory = baseDirectory;
        this.files = files;
    }

    public UpdatePack(String baseDirectory) {
        this.localDirectory = baseDirectory;
    }

    public UpdatePack getOutdatedFiles(UpdatePack recentPack) {
        UpdatePack toUpdate = new UpdatePack(this.localDirectory);
        Iterator var3 = recentPack.files.iterator();

        while (true) {
            UpdateFile file;
            UpdateFile current;
            do {
                if (!var3.hasNext()) {
                    return toUpdate;
                }

                file = (UpdateFile) var3.next();
                current = this.getFile(file.localLocation);
            } while (current != null && current.hash == file.hash);

            toUpdate.addFile(file);
        }
    }

    public void addFile(UpdateFile file) {
        this.files.add(file);
    }

    public UpdateFile getFile(String localLocation) {
        Iterator var2 = this.files.iterator();

        UpdateFile file;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            file = (UpdateFile) var2.next();
        } while (!file.localLocation.replace("\\", "/").replace("\\\\", "/").equalsIgnoreCase(localLocation.replace("\\", "/").replace("\\\\", "/")));

        return file;
    }
}
