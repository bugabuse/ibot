package com.farm.ibot.init;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class SessionProfile {
    private static SessionProfile[] profiles = new SessionProfile[0];
    public Session[] sessions;
    public String description;

    public SessionProfile(String description, Session... sessions) {
        this.sessions = sessions;
        this.description = description;
    }

    public static SessionProfile[] getProfiles() {
        return profiles;
    }

    public static SessionProfile fromSettingsFile() {
        FileReader reader = null;
        SessionProfile profile = null;
        File file = new File(Settings.SETTINGS_DIRECTORY.getAbsolutePath() + File.separator + "lastaccs.json");
        if (!file.exists()) {
            System.out.println("SessionsProfile file does not exist.");
            return null;
        } else {
            try {
                reader = new FileReader(file);
                profile = (SessionProfile) (new Gson()).fromJson(reader, SessionProfile.class);
            } catch (Exception var4) {
                var4.printStackTrace();
            }

            IOUtils.closeQuietly(reader);
            return profile;
        }
    }

    public static void load() {
        FileReader reader = null;

        try {
            reader = new FileReader(Settings.FILE_SESSION_PROFILES);
            profiles = (SessionProfile[]) (new Gson()).fromJson(reader, SessionProfile[].class);
            Arrays.sort(profiles, new Comparator<SessionProfile>() {
                @Override
                public int compare(SessionProfile o1, SessionProfile o2) {
                    return o1.description.compareTo(o2.description);
                }
            });
        } catch (FileNotFoundException var2) {
            var2.printStackTrace();
        }

        IOUtils.closeQuietly(reader);
    }

    public static SessionProfile multiplySession(SessionProfile p, int amount) {
        ArrayList<Session> sessions = new ArrayList();
        Session[] var3 = p.sessions;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Session s = var3[var5];

            for (int i = 0; i < amount; ++i) {
                sessions.add(new Session((AccountData) null, s.autostartScript));
            }
        }

        return new SessionProfile(p.description, (Session[]) sessions.toArray(new Session[0]));
    }

    public static SessionProfile forDescription(String description) {
        int amount = 1;
        if (description.contains("_")) {
            try {
                amount = Integer.parseInt(description.substring(description.lastIndexOf("_") + 1));
                description = description.substring(0, description.lastIndexOf("_"));
            } catch (Exception var12) {
            }
        }

        SessionProfile[] var2 = getProfiles();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            SessionProfile p = var2[var4];
            if (p.description.equalsIgnoreCase(description)) {
                ArrayList<Session> sessions = new ArrayList();
                Session[] var7 = p.sessions;
                int var8 = var7.length;

                for (int var9 = 0; var9 < var8; ++var9) {
                    Session s = var7[var9];

                    for (int i = 0; i < amount; ++i) {
                        sessions.add(new Session((AccountData) null, s.autostartScript));
                    }
                }

                return new SessionProfile(description, (Session[]) sessions.toArray(new Session[0]));
            }
        }

        return null;
    }

    public String toString() {
        return this.description;
    }
}
