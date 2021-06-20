package com.farm.ibot.api.util.web;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.net.URL;

public class CloudFlareHandler {
    public static boolean handle(WebClient wc, String html, String url) {
        try {
            URL uri = new URL(url);
            String javaScriptCode = parseJavascript(html);
            ScriptEngine engine = (new ScriptEngineManager()).getEngineByName("JavaScript");
            engine.put("engine", engine);
            Double tokenDouble = (Double) engine.eval(javaScriptCode);
            int token = tokenDouble.intValue() + uri.getHost().length();

            String jschl = html.split("name=\"jschl_vc\" value=\"")[1].split("\"")[0];
            String pass = html.split("name=\"pass\" value=\"")[1].split("\"")[0];
            Thread.sleep(3000L);
            wc.downloadString(uri.getProtocol() + "://" + uri.getHost() + "/cdn-cgi/l/chk_jschl?jschl_vc=" + jschl + "&pass=" + pass + "&jschl_answer=" + token);
            return true;
        } catch (Exception var10) {
            var10.printStackTrace();
            return false;
        }
    }

    public static String parseJavascript(String str) {
        str = str.substring(str.indexOf("var t,r,a,f,"));
        str = str.split("f.submit")[0];
        String newStr = "";
        String[] var2 = str.split("\n");
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String line = var2[var4];
            if (!line.contains("document.") && !line.contains("t.innerHTML=") && !line.contains("t =")) {
                newStr = newStr + line + "\n";
            }
        }

        newStr = newStr.replace("a.value = ", "").replace(" + t.length;", ";");
        return newStr;
    }
}
