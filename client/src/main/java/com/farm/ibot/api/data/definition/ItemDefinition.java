package com.farm.ibot.api.data.definition;

import com.farm.ibot.init.Settings;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class ItemDefinition {
    public static ItemDefinition[] definitions;
    public int id;
    public String name = "";
    public String[] interfaceOptions = new String[0];
    public String[] options = new String[0];
    public int notedID = -1;
    public int stackable = 0;
    public boolean members = false;

    public static void load() throws IOException {
        FileReader reader = new FileReader(Settings.ITEM_DEFINITION_FILE);
        definitions = (ItemDefinition[]) (new Gson()).fromJson(reader, ItemDefinition[].class);
        reader.close();
    }

    public static ItemDefinition forId(int id) {
        if (id >= 0 && definitions.length > id && definitions[id] != null && definitions[id].id == id) {
            return definitions[id];
        } else {
            ItemDefinition[] var1 = definitions;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                ItemDefinition def = var1[var3];
                if (def.id == id) {
                    return def;
                }
            }

            return definitions[0];
        }
    }

    public static ItemDefinition forName(String name) {
        ItemDefinition[] var1 = definitions;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ItemDefinition def = var1[var3];
            if (def != null && name.equalsIgnoreCase(def.name)) {
                return def;
            }
        }

        return null;
    }

    public boolean isStackable() {
        return this.stackable == 1 || this.isNoted();
    }

    public boolean isNoted() {
        return this.notedID == this.id;
    }

    public int getUnnotedId() {
        if (this.isStackable() && Objects.equals(forId(this.id - 1).name, this.name)) {
            return this.id - 1;
        } else {
            return Objects.equals(forId(this.id - 1).name, this.name) && forId(this.id - 1).notedID == this.id ? this.id - 1 : this.id;
        }
    }

    public boolean containsAction(String action) {
        return Arrays.stream(this.interfaceOptions).anyMatch((a) -> {
            return a.equalsIgnoreCase(action);
        });
    }
}
