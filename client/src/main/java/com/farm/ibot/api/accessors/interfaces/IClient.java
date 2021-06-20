package com.farm.ibot.api.accessors.interfaces;

import com.farm.ibot.api.accessors.CollisionMap;
import com.farm.ibot.api.accessors.EncryptedStream;
import com.farm.ibot.api.accessors.GameConfig;
import com.farm.ibot.api.accessors.World;

public interface IClient {
    int getMapAngle(Object var1);

    int getCurrentWorld(Object var1);

    int getBaseX(Object var1);

    int getBaseY(Object var1);

    int getIdleTime(Object var1);

    int getLoginState(Object var1);

    int getSelectionState(Object var1);

    int getGameCycle(Object var1);

    long getLastAction(Object var1);

    int getClickModifier(Object var1);

    String getPassword(Object var1);

    String getUsername(Object var1);

    int getPlane(Object var1);

    int[][][] getTileHeights(Object var1);

    byte[][][] getTileFlags(Object var1);

    CollisionMap[] getCollisionMaps(Object var1);

    World[] getWorlds(Object var1);

    Object getMouse(Object var1);

    Object getKeyboard(Object var1);

    int[] getRealSkillLevelArray(Object var1);

    int getGameTick(Object var1);

    int[] getExperienceArray(Object var1);

    int[] getCurrentLevelArray(Object var1);

    int getPlayerIndex(Object var1);

    GameConfig getGameConfig(Object var1);

    long getLastActionMod(Object var1);

    long getLastActionDifference(Object var1);

    int getZoom(Object var1);

    int getZoomExact(Object var1);

    int getMapScale(Object var1);

    int getMapOffset(Object var1);

    void setCurrentWorld(Object var1, Object var2);

    void setIdleTime(Object var1, Object var2);

    int hookClientSelectedItemId(Object var1);

    void setSelectedItemIndex(Object var1, Object var2);

    int hookGetSelectedItemIndex(Object var1);

    void setPassword(Object var1, Object var2);

    void setUsername(Object var1, Object var2);

    boolean isSpellSelected(Object var1);

    String hookGetMessage0(Object var1);

    String hookGetMessage1(Object var1);

    String hookGetMessage2(Object var1);

    EncryptedStream getEncryptedStream1(Object var1);

    String hookGetSelectedSpell(Object var1);

    void setZoom(Object var1, Object var2);

    void setZoomExact(Object var1, Object var2);

    void setLowCpu(Object var1, Object var2);

    void setInputEnabled(Object var1, Object var2);

    void setLowMemory(Object var1, Object var2);

    int getLastClickY(Object var1);

    int getLastClickX(Object var1);

    void setLastClickX(Object var1, Object var2);

    void setLastClickY(Object var1, Object var2);

    boolean isLowMemory(Object var1);

    void setLockLowCpuUntil(Object var1, long var2);

    int getViewportOffsetY(Object var1);

    int getViewportOffsetX(Object var1);

    int getViewportHeight(Object var1);

    int getViewportWidth(Object var1);
}
