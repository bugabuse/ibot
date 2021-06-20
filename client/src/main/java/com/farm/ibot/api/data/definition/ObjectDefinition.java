package com.farm.ibot.api.data.definition;

import com.farm.ibot.init.Settings;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public class ObjectDefinition {
    public static ObjectDefinition[] definitions;
    public int ID;
    public String name = "";
    public String[] actions = new String[0];
    public int width = -1;
    public int height = -1;
    public boolean walkable = false;

    public static void load() throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(Settings.OBJECT_DEFINITION_FILE);
        definitions = (ObjectDefinition[]) gson.fromJson(reader, ObjectDefinition[].class);
        reader.close();

    }

    public static ObjectDefinition forId(int id) {
        return id >= 0 && definitions.length > id && definitions[id] != null ? definitions[id] : definitions[0];
    }
}
