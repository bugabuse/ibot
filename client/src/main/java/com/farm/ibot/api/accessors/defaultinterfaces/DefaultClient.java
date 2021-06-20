package com.farm.ibot.api.accessors.defaultinterfaces;

import com.farm.ibot.api.accessors.*;
import com.farm.ibot.api.accessors.interfaces.IClient;

public class DefaultClient implements IClient {
    public int getMapScale(Object instance) {
        return (Integer) Wrapper.getStatic("Client.MapScale");
    }

    public int getMapOffset(Object instance) {
        return (Integer) Wrapper.getStatic("Client.MapOffset");
    }

    public int getMapAngle(Object instance) {
        return (Integer) Wrapper.getStatic("Client.MapAngle");
    }

    public int getCurrentWorld(Object instance) {
        return (Integer) Wrapper.getStatic("Client.CurrentWorld");
    }

    public void setCurrentWorld(Object instance, Object world) {
        Wrapper.setStatic("Client.CurrentWorld", world);
    }

    public int getBaseX(Object instance) {
        return (Integer) Wrapper.getStatic("Client.BaseX");
    }

    public int getBaseY(Object instance) {
        return (Integer) Wrapper.getStatic("Client.BaseY");
    }

    public int getIdleTime(Object instance) {
        return (Integer) Wrapper.getStatic("Client.IdleTime");
    }

    public void setIdleTime(Object instance, Object time) {
        Wrapper.setStatic("Client.IdleTime", time);
    }

    public int getLoginState(Object instance) {
        return (Integer) Wrapper.getStatic("Client.LoginState");
    }

    public int hookClientSelectedItemId(Object instance) {
        return (Integer) Wrapper.getStatic("Client.SelectedItemId");
    }

    public void setSelectedItemIndex(Object instance, Object value) {
        Wrapper.setStatic("Client.SelectedItemId", value);
    }

    public int hookGetSelectedItemIndex(Object instance) {
        return (Integer) Wrapper.getStatic("Client.SelectedItemIndex");
    }

    public int getSelectionState(Object instance) {
        return (Integer) Wrapper.getStatic("Client.SelectionState");
    }

    public int getGameCycle(Object instance) {
        return (Integer) Wrapper.getStatic("Client.GameCycle");
    }

    public long getLastAction(Object instance) {
        return (Long) Wrapper.getStatic("Client.LastAction");
    }

    public int getClickModifier(Object instance) {
        return (Integer) Wrapper.getStatic("Client.ClickModifier");
    }

    public String getPassword(Object instance) {
        return (String) Wrapper.getStatic("Client.Password");
    }

    public void setPassword(Object instance, Object password) {
        Wrapper.setStatic("Client.Password", password);
    }

    public String getUsername(Object instance) {
        return (String) Wrapper.getStatic("Client.Username");
    }

    public void setUsername(Object instance, Object value) {
        Wrapper.setStatic("Client.Username", value);
    }

    public int getPlane(Object instance) {
        return (Integer) Wrapper.getStatic("Client.Plane");
    }

    public int[][][] getTileHeights(Object instance) {
        return (int[][][]) Wrapper.getStatic("Client.TileHeights");
    }

    public byte[][][] getTileFlags(Object instance) {
        return (byte[][][]) Wrapper.getStatic("Client.TileFlags");
    }

    public CollisionMap[] getCollisionMaps(Object instance) {
        return (CollisionMap[]) Wrapper.getStatic("Client.CollisionMaps", CollisionMap[].class);
    }

    public World[] getWorlds(Object instance) {
        return (World[]) Wrapper.getStatic("Client.worlds", World[].class);
    }

    public Object getMouse(Object instance) {
        return Wrapper.getStatic("Client.Mouse");
    }

    public Object getKeyboard(Object instance) {
        return Wrapper.getStatic("Client.Keyboard");
    }

    public int[] getRealSkillLevelArray(Object instance) {
        return (int[]) Wrapper.getStatic("Client.RealSkillLevelArray");
    }

    public int getGameTick(Object instance) {
        return (Integer) Wrapper.getStatic("Client.GameTick");
    }

    public int[] getExperienceArray(Object instance) {
        return (int[]) Wrapper.getStatic("Client.ExperienceArray");
    }

    public int[] getCurrentLevelArray(Object instance) {
        return (int[]) Wrapper.getStatic("Client.CurrentLevelArray");
    }

    public int getPlayerIndex(Object instance) {
        return (Integer) Wrapper.getStatic("Client.PlayerIndex");
    }

    public GameConfig getGameConfig(Object instance) {
        return (GameConfig) Wrapper.getStatic("Client.GameConfigObject", GameConfig.class);
    }

    public boolean isSpellSelected(Object instance) {
        return (Boolean) Wrapper.getStatic("Client.SpellSelected");
    }

    public String hookGetMessage0(Object instance) {
        return (String) Wrapper.getStatic("Client.message0");
    }

    public String hookGetMessage1(Object instance) {
        return (String) Wrapper.getStatic("Client.message1");
    }

    public String hookGetMessage2(Object instance) {
        return (String) Wrapper.getStatic("Client.message2");
    }

    public EncryptedStream getEncryptedStream1(Object instance) {
        return (EncryptedStream) Wrapper.getStatic("Client.encryptedStream1", EncryptedStream.class);
    }

    public String hookGetSelectedSpell(Object instance) {
        return (String) Wrapper.getStatic("Client.SelectedSpell");
    }

    public long getLastActionMod(Object instance) {
        return (Long) Wrapper.getStatic("Client.LastActionMod");
    }

    public long getLastActionDifference(Object instance) {
        return (Long) Wrapper.getStatic("Client.LastActionDifference");
    }

    public int getZoom(Object instance) {
        return (Integer) Wrapper.getStatic("Client.zoom");
    }

    public int getZoomExact(Object instance) {
        return (Integer) Wrapper.getStatic("Client.zoomExact");
    }

    public void setZoom(Object instance, Object value) {
        Wrapper.setStatic("Client.zoom", value);
    }

    public void setZoomExact(Object instance, Object value) {
        Wrapper.setStatic("Client.zoomExact", value);
    }

    public void setLowCpu(Object instance, Object value) {
        Wrapper.setStatic("Client.lowCpuEnabled", value);
    }

    public void setInputEnabled(Object instance, Object value) {
        Wrapper.setStatic("Client.enableInput", value);
    }

    public void setLowMemory(Object instance, Object value) {
        Wrapper.setStatic("Client.lowMemory", value);
    }

    public int getLastClickY(Object instance) {
        return (Integer) Wrapper.getStatic("Client.lastClickX");
    }

    public int getLastClickX(Object instance) {
        return (Integer) Wrapper.getStatic("Client.lastClickY");
    }

    public void setLastClickX(Object instance, Object value) {
        Wrapper.setStatic("Client.lastClickX", value);
    }

    public void setLastClickY(Object instance, Object value) {
        Wrapper.setStatic("Client.lastClickY", value);
    }

    public boolean isLowMemory(Object instance) {
        return (Boolean) Wrapper.getStatic("Client.lowMemory");
    }

    public void setLockLowCpuUntil(Object instance, long time) {
        Wrapper.setStatic("Client.lockLowCpuUntil", time);
    }

    public int getViewportOffsetY(Object o) {
        return (Integer) Wrapper.getStatic("Client.viewportOffsetY");
    }

    public int getViewportOffsetX(Object o) {
        return (Integer) Wrapper.getStatic("Client.viewportOffsetX");
    }

    public int getViewportHeight(Object o) {
        return (Integer) Wrapper.getStatic("Client.viewportHeight");
    }

    public int getViewportWidth(Object o) {
        return (Integer) Wrapper.getStatic("Client.viewportWidth");
    }
}
