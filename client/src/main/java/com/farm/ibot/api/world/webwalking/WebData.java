package com.farm.ibot.api.world.webwalking;

import com.farm.ibot.api.world.webwalking.node.WebNode;
import com.farm.ibot.init.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

public class WebData {
    private static ArrayList<WebNode> nodes = new ArrayList();

    public static void load() {
        try {
            WebData.nodes = new ArrayList();
            FileReader reader = new FileReader(Settings.WEBDATA_FILE);
            Gson gson = (new GsonBuilder()).registerTypeAdapter(WebNode.class, new WebNodeParser()).create();
            WebNode[] nodes = (WebNode[]) gson.fromJson(reader, WebNode[].class);

            for (int i = 0; i < nodes.length; ++i) {
                WebNode node = nodes[i];
                node.setDestinationNodes(nodes);
            }

            Collections.addAll(WebData.nodes, nodes);
            reader.close();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public static ArrayList<WebNode> getNodes() {
        return nodes;
    }

    public static void export() {
        try {
            FileWriter writer = new FileWriter(Settings.WEBDATA_FILE);
            (new GsonBuilder()).excludeFieldsWithModifiers(new int[]{128}).create().toJson(nodes, writer);
            writer.close();
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }
}
