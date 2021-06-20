package com.farm.ibot.core.script;

import com.farm.ibot.Main;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.SafeArrayList;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.reflection.classloader.GameInjectionClassLoader;
import com.farm.ibot.core.reflection.classloader.Injector;
import com.farm.ibot.init.Session;
import com.farm.ibot.init.SessionProfile;
import com.farm.ibot.init.Settings;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

public class ScriptLoader {
    private String lastScriptName;
    private ScriptHandler scriptHandler;

    public ScriptLoader(ScriptHandler scriptHandler) {
        this.scriptHandler = scriptHandler;
    }

    public String getLastScriptName() {
        return this.lastScriptName;
    }

    public BotScript loadLast() throws Exception {
        return this.load(this.lastScriptName);
    }

    public void loadEventHandlers() {
    }

    public BotScript[] loadScripts() throws Exception {
        ArrayList<BotScript> temp = new ArrayList();
        URLClassLoader loader = new URLClassLoader(new URL[]{(new File(Settings.SCRIPTS_DIRECTORY.getAbsolutePath() + File.separator)).toURI().toURL()}, this.scriptHandler.getBot().getGameLoader().getClassLoader().getClassLoader());
        if (Settings.useInjection) {
            (new Injector(((GameInjectionClassLoader) this.scriptHandler.getBot().getGameLoader().getClassLoader()).cp)).loadGeneratedAccessorsToApi(loader, this.scriptHandler.getBot());
        }


        String packageStr = "com.farm.scripts";
        Reflections reflections = new Reflections((new ConfigurationBuilder()).setScanners(new Scanner[]{new SubTypesScanner()})
                .setUrls(ClasspathHelper.forPackage(packageStr, new ClassLoader[0]))
                .filterInputsBy((new FilterBuilder()).includePackage(packageStr)));
        Set clazzez = reflections.getSubTypesOf(BotScript.class);
        Set classes = reflections.getSubTypesOf(MultipleStrategyScript.class);
        classes.addAll(clazzez);

        classes.stream().forEach((c) -> {
            try {
                BotScript script = (BotScript) ((Class<?>) c).newInstance();
                script.scriptHandler = this.scriptHandler;
                temp.add(script);
            } catch (Exception e) {
            }
        });

        if (0 != 9)
            return (BotScript[]) temp.toArray(new BotScript[temp.size()]);


        Stream<Path> paths = Files.walk(Paths.get(Settings.SCRIPTS_DIRECTORY.getAbsolutePath()));
        Throwable var4 = null;

        try {
            paths.sorted().filter((x$0) -> {
                return Files.isRegularFile(x$0, new LinkOption[0]);
            }).filter((f) -> {
                return f.toString().endsWith(".class");
            }).forEach((f) -> {
                try {
                    String name = f.toString().replace(Settings.SCRIPTS_DIRECTORY.getAbsolutePath(), "").replace(File.separator, ".").substring(1).replace(".class", "");
                    Class clazz = loader.loadClass(name);
                    if (BotScript.class.isAssignableFrom(clazz)) {
                        try {
                            BotScript script = (BotScript) clazz.newInstance();
                            script.scriptHandler = this.scriptHandler;
                            temp.add(script);
                        } catch (Exception e) {
                            //e.printStackTrace();
                            Debug.log("Script failed to load: " + e.getLocalizedMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        } catch (Throwable var13) {
            var4 = var13;
            throw var13;
        } finally {
            if (paths != null) {
                if (var4 != null) {
                    try {
                        paths.close();
                    } catch (Throwable var12) {
                        var4.addSuppressed(var12);
                    }
                } else {
                    paths.close();
                }
            }

        }

        return (BotScript[]) temp.toArray(new BotScript[temp.size()]);
    }

    public BotScript load(String name) throws Exception {
        this.lastScriptName = name;

        BotScript[] available = new BotScript[0];

        try {
            available = this.loadScripts();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SafeArrayList<BotScript> scripts = new SafeArrayList();
        this.scriptHandler.setScriptQueue(scripts);
        String[] var4 = name.split("\\|");
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String str = var4[var6];
            String args = "";
            if (str.indexOf("(") > 0 && str.lastIndexOf(")") > str.indexOf("(")) {
                args = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")"));
                str = str.substring(0, str.indexOf("("));
            }

            BotScript[] var9 = available;
            int var10 = available.length;

            for (int var11 = 0; var11 < var10; ++var11) {
                BotScript script = var9[var11];
                if (script.getName().equalsIgnoreCase(str)) {
                    BotScript copyOfScript = (BotScript) script.getClass().newInstance();
                    copyOfScript.scriptHandler = this.scriptHandler;
                    copyOfScript.startArguments = args;
                    scripts.add(copyOfScript);
                    copyOfScript.onLoad();
                }
            }
        }

        return (BotScript) scripts.get(0);
    }

    public BotScript loadWithList() throws Exception {
        BotScript[] scripts = this.loadScripts();
        ArrayList<String> scriptNames = new SafeArrayList();
        BotScript[] var3 = scripts;
        int var4 = scripts.length;

        int var5;
        for (var5 = 0; var5 < var4; ++var5) {
            BotScript script = var3[var5];
            scriptNames.add(script.getName());
        }

        SessionProfile[] var11 = SessionProfile.getProfiles();
        var4 = var11.length;

        for (var5 = 0; var5 < var4; ++var5) {
            SessionProfile profile = var11[var5];
            Session[] var7 = profile.sessions;
            int var8 = var7.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                Session session = var7[var9];
                if (!scriptNames.contains(session.autostartScript)) {
                    scriptNames.add(session.autostartScript);
                }
            }
        }

        if (scripts.length > 0) {
            Collections.sort(scriptNames);
            String[] scriptNamesArray = (String[]) scriptNames.toArray(new String[scriptNames.size()]);
            String scriptName = (String) JOptionPane.showInputDialog(Main.frame, "Select script", "Script", 0, (Icon) null, scriptNamesArray, scriptNamesArray[0]);
            this.lastScriptName = scriptName;
            return this.load(scriptName);
        } else {
            Bot.getSelectedBot().getScriptHandler().stop();
            return null;
        }
    }
}
