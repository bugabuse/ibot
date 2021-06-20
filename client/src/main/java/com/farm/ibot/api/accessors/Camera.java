package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.ICamera;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;

public class Camera extends Wrapper {
    public Camera(Object instance) {
        super(instance);
    }

    public static ICamera getInterface() {
        return Bot.get().accessorInterface.cameraReflectionInterface;
    }

    @HookName("Camera.X")
    public static int getX() {
        return getInterface().getX(null);
    }

    @HookName("Camera.X")
    public static void setX(int value) {
        getInterface().setX(null, value);
    }

    @HookName("Camera.Y")
    public static int getY() {
        return getInterface().getY(null);
    }

    @HookName("Camera.Y")
    public static void setY(int value) {
        getInterface().setY(null, value);
    }

    @HookName("Camera.Z")
    public static int getZ() {
        return getInterface().getZ(null);
    }

    @HookName("Camera.Z")
    public static void setZ(int value) {
        getInterface().setZ(null, value);
    }

    @HookName("Camera.Pitch")
    public static int getPitch() {
        return getInterface().getPitch(null);
    }

    @HookName("Camera.Pitch")
    public static void setPitch(int value) {
        getInterface().setPitch(null, value);
    }

    @HookName("Camera.Yaw")
    public static int getYaw() {
        return getInterface().getYaw(null);
    }

    @HookName("Camera.Yaw")
    public static void setYaw(int value) {
        getInterface().setYaw(null, value);
    }
}
