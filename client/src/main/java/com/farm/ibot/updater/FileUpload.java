// Decompiled with: Procyon 0.5.36
package com.farm.ibot.updater;

import com.farm.ibot.api.util.web.Authentication;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUpload {
    public static void upload(final File file, String name) throws IOException, InterruptedException {
        name = name.replace("\\", "/").replace("\\\\", "/");
        System.out.println("To: http://api.hax0r.farm:8080/update.php?filename=download/" + name);
        final HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL("http://api.hax0r.farm:8080/update.php?filename=download/" + name).openConnection();
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setRequestMethod("POST");
        Authentication.authRequest(httpUrlConnection);
        final OutputStream os = httpUrlConnection.getOutputStream();
        Thread.sleep(1000L);
        final BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file));
        final long totalByte = file.length();
        int byteTrasferred = 0;
        for (int i = 0; i < totalByte; ++i) {
            os.write(fis.read());
            byteTrasferred = i + 1;
        }
        os.close();
        final BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        in.close();
        fis.close();
    }
}
