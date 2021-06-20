package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.ICamera;

public class DefaultCamera implements ICamera {
    public int getX(Object instance) {
        return (Integer) Wrapper.get("Camera.X", instance);
    }

    public int getY(Object instance) {
        return (Integer) Wrapper.get("Camera.Y", instance);
    }

    public int getZ(Object instance) {
        return (Integer) Wrapper.get("Camera.Z", instance);
    }

    public int getPitch(Object instance) {
        return (Integer) Wrapper.get("Camera.Pitch", instance);
    }

    public int getYaw(Object instance) {
        return (Integer) Wrapper.get("Camera.Yaw", instance);
    }

    public void setX(Object instance, Object value) {
        Wrapper.set("Camera.X", instance, value);
    }

    public void setY(Object instance, Object value) {
        Wrapper.set("Camera.Y", instance, value);
    }

    public void setZ(Object instance, Object value) {
        Wrapper.set("Camera.Z", instance, value);
    }

    public void setPitch(Object instance, Object value) {
        Wrapper.set("Camera.Pitch", instance, value);
    }

    public void setYaw(Object instance, Object value) {
        Wrapper.set("Camera.Yaw", instance, value);
    }
}
