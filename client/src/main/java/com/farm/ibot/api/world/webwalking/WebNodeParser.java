package com.farm.ibot.api.world.webwalking;

import com.farm.ibot.api.world.webwalking.node.NpcWebNode;
import com.farm.ibot.api.world.webwalking.node.ObjectWebNode;
import com.farm.ibot.api.world.webwalking.node.WebNode;
import com.farm.ibot.api.world.webwalking.node.WebNodeType;
import com.google.gson.*;

import java.lang.reflect.Type;

public class WebNodeParser implements JsonDeserializer<WebNode> {
    public WebNode deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = (JsonObject) json;
        if (obj.has("type")) {
            WebNodeType type = (WebNodeType) (new Gson()).fromJson(obj.get("type"), WebNodeType.class);
            if (type != null) {
                return (WebNode) (new Gson()).fromJson(obj, type.getTypeClass());
            }
        } else {

        }

        if (obj.has("objectName")) {
            return (WebNode) (new Gson()).fromJson(obj, ObjectWebNode.class);
        } else {
            return obj.has("npcName") ? (WebNode) (new Gson()).fromJson(obj, NpcWebNode.class) : (WebNode) (new Gson()).fromJson(obj, WebNode.class);
        }
    }
}
