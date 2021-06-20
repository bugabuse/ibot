package com.farm.ibot.api.data.definition;

import com.farm.ibot.init.Settings;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public class NpcDefinition {
    public static final NpcDefinition nullDefinition = new NpcDefinition();
    public static NpcDefinition[] definitions;
    public int ID = -1;
    public String name = "";
    public String[] actions = new String[0];
    public int width = -1;
    public int height = -1;
    public int walkAnimationID = -1;
    public int combatLevel = -1;

    public static void load() throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(Settings.NPC_DEFINITION_FILE);
        definitions = (NpcDefinition[]) gson.fromJson(reader, NpcDefinition[].class);
        reader.close();
    }

    public static NpcDefinition forId(int id) {
        if (id > -1 && definitions.length > id && definitions[id] != null && definitions[id].ID == id) {
            return definitions[id];
        } else {
            NpcDefinition[] var1 = definitions;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                NpcDefinition def = var1[var3];
                if (def.ID == id) {
                    return def;
                }
            }

            return nullDefinition;
        }
    }

    public static NpcDefinition[] getDefinitions() {
        return definitions;
    }

    public String[] getActions() {
        return this.actions;
    }

    public int getId() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public int getWidth() {
        return this.width;
    }

    public int getWalkAnimationID() {
        return this.walkAnimationID;
    }

    public int getHeight() {
        return this.height;
    }

    public int getCombatLevel() {
        return this.combatLevel;
    }

    public boolean containsAction(String action) {
        if (action != null) {
            String[] var2 = this.actions;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String a = var2[var4];
                if (action.equals(a)) {
                    return true;
                }
            }
        }

        return false;
    }
}
