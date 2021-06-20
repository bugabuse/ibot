package com.farm.scripts.humanfletcher.api.action.wrapper;

public class KeyAction extends Action {
    public KeyAction.Type type;
    public int key;

    public KeyAction(long time, String type, int key) {
        super("", time);
        if (type.contains("press")) {
            this.type = KeyAction.Type.PRESS;
        } else if (type.contains("type")) {
            this.type = KeyAction.Type.TYPE;
        } else if (type.contains("release")) {
            this.type = KeyAction.Type.RELEASE;
        }

        this.key = key;
    }

    public static enum Type {
        RELEASE,
        PRESS,
        TYPE;
    }
}
