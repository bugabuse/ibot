package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IClient;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interfaces.Filter;
import com.farm.ibot.api.methods.WorldHopping;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.core.Bot;
import com.farm.ibot.init.Hooks;
import com.farm.ibot.init.Settings;
import com.google.common.primitives.Ints;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Client extends Wrapper {
    public Client(Object instance) {
        super(instance);
    }

    public static void processMenuAction(int arg1, int arg2, int arg3, int arg4, String sArg1, String sArg2, int arg5, int arg6) {
        invokeStatic("Client.processMenuAction", new Object[]{arg1, arg2, arg3, arg4, sArg1, sArg2, arg5, arg6, 111});
    }

    public static IClient getClientReflectionInterface() {
        return Bot.get().accessorInterface.clientReflectionInterface;
    }

    @HookName("Client.MapAngle")
    public static int getMapAngle() {
        return getClientReflectionInterface().getMapAngle(null);
    }

    @HookName("Client.lowMemory")
    public static boolean isLowMemory() {
        return getClientReflectionInterface().isLowMemory(null);
    }

    @HookName("Client.lowMemory")
    public static void setLowMemory(boolean lowMemory) {
    }

    @HookName("Client.CurrentWorld")
    public static int getCurrentWorld() {
        return getClientReflectionInterface().getCurrentWorld(null);
    }

    @HookName("Client.CurrentWorld")
    public static void setCurrentWorld(int world) {
        getClientReflectionInterface().setCurrentWorld(null, world);
    }

    @HookName("Client.BaseX")
    public static int getBaseX() {
        return getClientReflectionInterface().getBaseX(null);
    }

    @HookName("Client.BaseY")
    public static int getBaseY() {
        return getClientReflectionInterface().getBaseY(null);
    }

    @HookName("Client.IdleTime")
    public static int getIdleTime() {
        return getClientReflectionInterface().getIdleTime(null);
    }

    @HookName("Client.IdleTime")
    public static void setIdleTime(int time) {
        getClientReflectionInterface().setIdleTime(null, time);
    }

    @HookName("Client.LoginState")
    public static int getLoginState() {
        return getClientReflectionInterface().getLoginState(null);
    }

    @HookName("Client.SelectionState")
    public static int getSelectionState() {
        return getClientReflectionInterface().getSelectionState(null);
    }

    @HookName("Client.GameCycle")
    public static int getGameCycle() {
        return getClientReflectionInterface().getGameCycle(null);
    }

    @HookName("Client.LastAction")
    public static long getLastAction() {
        return getClientReflectionInterface().getLastAction(null);
    }

    @HookName("Client.ClickModifier")
    public static int getClickModifier() {
        return getClientReflectionInterface().getClickModifier(null);
    }

    @HookName("Client.Password")
    public static String getPassword() {
        return getClientReflectionInterface().getPassword(null);
    }

    @HookName("Client.Password")
    public static void setPassword(String password) {
        getClientReflectionInterface().setPassword(null, password);
    }

    @HookName("Client.Username")
    public static String getUsername() {
        return getClientReflectionInterface().getUsername(null);
    }

    @HookName("Client.Username")
    public static void setUsername(String username) {
        getClientReflectionInterface().setUsername(null, username);
    }

    @HookName("Client.Plane")
    public static int getPlane() {
        return getClientReflectionInterface().getPlane(null);
    }

    @HookName("Client.TileHeights")
    public static int[][][] getTileHeights() {
        return getClientReflectionInterface().getTileHeights(null);
    }

    @HookName("Client.TileFlags")
    public static byte[][][] getTileFlags() {
        return getClientReflectionInterface().getTileFlags(null);
    }

    @HookName("Client.CollisionMaps")
    public static CollisionMap[] getCollisionMaps() {
        return getClientReflectionInterface().getCollisionMaps(null);
    }

    @HookName("Client.worlds")
    public static World[] getWorlds() {
        return getClientReflectionInterface().getWorlds(null);
    }

    @HookName("Client.Mouse")
    public static Object getMouse() {
        return getClientReflectionInterface().getMouse(null);
    }

    @HookName("Client.Keyboard")
    public static Object getKeyboard() {
        return getClientReflectionInterface().getKeyboard(null);
    }

    @HookName("Client.RealSkillLevelArray")
    public static int[] getRealSkillLevelArray() {
        return getClientReflectionInterface().getRealSkillLevelArray(null);
    }

    @HookName("Client.GameTick")
    public static int getGameTick() {
        return getClientReflectionInterface().getGameTick(null);
    }

    @HookName("Client.ExperienceArray")
    public static int[] getExperienceArray() {
        return getClientReflectionInterface().getExperienceArray(null);
    }

    @HookName("Client.CurrentLevelArray")
    public static int[] getCurrentLevelArray() {
        return getClientReflectionInterface().getCurrentLevelArray(null);
    }

    @HookName("Client.PlayerIndex")
    public static int getPlayerIndex() {
        return getClientReflectionInterface().getPlayerIndex(null);
    }

    @HookName("Client.GameConfigObject")
    public static GameConfig getGameConfig() {
        return getClientReflectionInterface().getGameConfig(null);
    }

    @HookName("Client.LastActionMod")
    public static long getLastActionMod() {
        return getClientReflectionInterface().getLastActionMod(null);
    }

    @HookName("Client.LastActionDifference")
    public static long getLastActionDifference() {
        return getClientReflectionInterface().getLastActionDifference(null);
    }

    @HookName("Client.lastClickX")
    public static int getLastClickX() {
        return getClientReflectionInterface().getLastClickX(null);
    }

    @HookName("Client.lastClickX")
    public static void setLastClickX(int value) {
        getClientReflectionInterface().setLastClickX(null, value);
    }

    @HookName("Client.lastClickX")
    public static int getLastClickY() {
        return getClientReflectionInterface().getLastClickY(null);
    }

    @HookName("Client.lastClickY")
    public static void setLastClickY(int value) {
        getClientReflectionInterface().setLastClickY(null, value);
    }

    @HookName("Client.zoom")
    public static int getZoom() {
        return getClientReflectionInterface().getZoom(null);
    }

    @HookName("Client.zoom")
    public static void setZoom(int zoom) {
        getClientReflectionInterface().setZoom(null, zoom);
    }

    @HookName("Client.viewportWidth")
    public static int getViewportWidth() {
        return getClientReflectionInterface().getViewportWidth(null);
    }

    @HookName("Client.viewportHeight")
    public static int getViewportHeight() {
        return getClientReflectionInterface().getViewportHeight(null);
    }

    @HookName("Client.viewportOffsetX")
    public static int getViewportOffsetX() {
        return getClientReflectionInterface().getViewportOffsetX(null);
    }

    @HookName("Client.viewportOffsetY")
    public static int getViewportOffsetY() {
        return getClientReflectionInterface().getViewportOffsetY(null);
    }

    @HookName("Client.zoomExact")
    public static int getZoomExact() {
        return getClientReflectionInterface().getZoomExact(null);
    }

    @HookName("Client.zoomExact")
    public static void setZoomExact(short zoom) {
        getClientReflectionInterface().setZoomExact(null, zoom);
    }

    @HookName("Client.MapScale")
    public static int getMapScale() {
        return getClientReflectionInterface().getMapScale(null);
    }

    @HookName("Client.MapOffset")
    public static int getMapOffset() {
        return getClientReflectionInterface().getMapOffset(null);
    }

    @HookName("Client.SelectedItemId")
    private static int hookClientSelectedItemId() {
        return getClientReflectionInterface().hookClientSelectedItemId(null);
    }

    @HookName("Client.SelectedItemIndex")
    private static int hookGetSelectedItemIndex() {
        return getClientReflectionInterface().hookGetSelectedItemIndex(null);
    }

    @HookName("Client.SpellSelected")
    public static boolean isSpellSelected() {
        return getClientReflectionInterface().isSpellSelected(null);
    }

    @HookName("Client.message0")
    private static String hookGetMessage0() {
        return getClientReflectionInterface().hookGetMessage0(null);
    }

    @HookName("Client.message1")
    private static String hookGetMessage1() {
        return getClientReflectionInterface().hookGetMessage1(null);
    }

    @HookName("Client.message2")
    private static String hookGetMessage2() {
        return getClientReflectionInterface().hookGetMessage2(null);
    }

    @HookName("Client.encryptedStream1")
    public static EncryptedStream getEncryptedStream1() {
        return getClientReflectionInterface().getEncryptedStream1(null);
    }

    @HookName("Client.SelectedSpell")
    public static String hookGetSelectedSpell() {
        return getClientReflectionInterface().hookGetSelectedSpell(null);
    }

    @HookName("Client.lowCpuEnabled")
    public static void setLowCpu(boolean enabled) {
        if (Settings.useInjection) {
            getClientReflectionInterface().setLowCpu(null, enabled);
        }

    }

    @HookName("Client.lockLowCpuUntil")
    public static void setLockLowCpuUntil(long time) {
        if (Settings.useInjection) {
            getClientReflectionInterface().setLockLowCpuUntil(null, time);
        }

    }

    @HookName("Client.enableInput")
    public static void setInputEnabled(boolean enabled) {
        getClientReflectionInterface().setInputEnabled(null, enabled);
    }

    @HookName("Client.widgetRoot")
    public static int getWidgetRoot() {
        return (Integer) Wrapper.getStatic("Client.widgetRoot");
    }

    @HookName("Client.widgetRoot")
    public static void setWidgetRoot(int value) {
        Wrapper.setStatic("Client.widgetRoot", value);
    }

    @HookName("Client.loginAccountState")
    public static int getLoginAccountState() {
        return (Integer) Wrapper.getStatic("Client.loginAccountState");
    }

    @HookName("Client.loginAccountState")
    public static void setLoginAccountState(int state) {
        Wrapper.setStatic("Client.loginAccountState", state);
    }

    @HookName("Client.loginScreenId")
    public static int getLoginScreenId() {
        return (Integer) Wrapper.getStatic("Client.loginScreenId");
    }

    @HookName("Client.loginScreenId")
    public static void setLoginScreenId(int value) {
        Wrapper.setStatic("Client.loginScreenId", value);
    }

    @HookName("Client.randomDatContent")
    public static void setRandomDatContent(byte[] randomDatContent) {
        Wrapper.setStatic("Client.randomDatContent", randomDatContent);
    }

    @HookName("Client.switchToWorld")
    public static void switchToWorld(World world) {
        Wrapper.invokeStatic("Client.switchToWorld", world.instance, Hooks.forName("Client.switchToWorld").getterMultipler.byteValue());
    }

    @HookName("Client.twoFactorPin")
    public static String getTwoFactorPin() {
        return (String) Wrapper.getStatic("Client.twoFactorPin");
    }

    @HookName("Client.twoFactorPin")
    public static void setTwoFactorPin(String value) {
        Wrapper.setStatic("Client.twoFactorPin", value);
    }

    public static int getTotalLevel() {
        return Arrays.stream(Skill.values()).mapToInt(Skill::getRealLevel).sum();
    }

    public static int getSelectedItemIndex() {
        return getSelectionState() == 1 ? hookGetSelectedItemIndex() : -1;
    }

    @HookName("Client.SelectedItemId")
    public static void setSelectedItemIndex(int index) {
        getClientReflectionInterface().setSelectedItemIndex(null, index);
    }

    public static Canvas getOriginalCanvas() {
        return GameShell.getInstance().getCanvas();
    }

    public static String getLoginMessage() {
        return hookGetMessage0() + hookGetMessage1() + hookGetMessage2();
    }

    public static String getSelectedSpell() {
        String str = hookGetSelectedSpell();
        return str != null && isSpellSelected() ? StringUtils.formatColorsString(str) : "";
    }

    public static void deselectItem() {
        if (getSelectedItemId() > 0) {
            Mouse.naturalMove(538 + Random.next(-5, 5), 158 + Random.next(-5, 5));
            Mouse.click(1);
        }

    }

    public static boolean isInGame() {
        return getLoginState() == 30 && Widgets.isValid(160);
    }

    public static int getRunEnergy() {
        Widget widget = Widgets.get(160, 23);
        String text = widget != null ? widget.getText() : "";
        return text.length() > 0 ? Integer.parseInt(text) : 0;
    }

    public static int getSelectedItemId() {
        return getSelectionState() == 1 ? hookClientSelectedItemId() : -1;
    }

    public static void setSelectedItemId(int id) {
        setStatic(id);
    }

    public static ArrayList<World> getWorldList() {
        return getWorlds((f) -> {
            return true;
        });
    }

    public static ArrayList<World> getWorlds(Filter<World> filter) {
        if (getWorlds() == null) {

            if (!isInGame()) {
                WorldHopping.openWorldHop();
            }
        }

        ArrayList<World> temp = new ArrayList();
        World[] var2 = getWorlds();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            World w = var2[var4];
            if (filter.accept(w)) {
                temp.add(w);
            }
        }

        return temp;
    }

    public static List<Integer> getF2pWorlds() {
        return getWorlds() != null ? (List) getWorlds(World::isSafeF2p).stream().map(World::getId).collect(Collectors.toList()) : Ints.asList(WorldHopping.F2P_WORLDS);
    }

    public static List<Integer> getP2pWorlds() {
        return getWorlds() != null ? (List) getWorlds(World::isSafeP2p).stream().map(World::getId).collect(Collectors.toList()) : Ints.asList(WorldHopping.P2P_WORLDS);
    }
}
