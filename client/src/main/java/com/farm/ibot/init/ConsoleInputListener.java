// Decompiled with: FernFlower
package com.farm.ibot.init;

import com.farm.ibot.Main;
import com.farm.ibot.api.accessors.Character;
import com.farm.ibot.api.accessors.*;
import com.farm.ibot.api.data.definition.NpcDefinition;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.interact.action.data.ItemMethod;
import com.farm.ibot.api.interact.action.impl.ItemAction;
import com.farm.ibot.api.interact.action.impl.ObjectAction;
import com.farm.ibot.api.methods.*;
import com.farm.ibot.api.methods.banking.Bank;
import com.farm.ibot.api.methods.banking.GrandExchange;
import com.farm.ibot.api.methods.banking.GrandExchangeOffer;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.entities.Players;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.KeyBindingConfig;
import com.farm.ibot.api.methods.input.Login;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.methods.shop.Shop;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.Terminal;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.util.web.WebClient;
import com.farm.ibot.api.world.WorldData;
import com.farm.ibot.api.world.webwalking.WebData;
import com.farm.ibot.api.wrapper.GameTab;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.reflection.Hook;
import com.farm.ibot.core.reflection.Reflection;
import com.farm.ibot.core.script.impl.debuggers.*;
import com.farm.ibot.proxy.Proxy;
import com.farm.ibot.proxy.ProxyManager;
import com.farm.ibot.updater.Updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

public class ConsoleInputListener {
    private static void run(Runnable runnable) {
        runnable.run();
    }

    public static void init() {
        if (Settings.devMode) {
            (new Thread(() -> {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String line = "";

                try {
                    for (; (line = in.readLine()) != null; Time.sleep(1000)) {
                        try {
                            onCommand(line);
                        } catch (Exception var4) {
                            var4.printStackTrace();
                        }
                    }
                } catch (IOException var5) {
                    var5.printStackTrace();
                }

                try {
                    in.close();
                } catch (IOException var3) {
                    var3.printStackTrace();
                }

            })).start();
        }
    }

    private static void onCommand(String line) throws Exception {
        Bot.currentThreadBot = Bot.getSelectedBot();
        if (line.startsWith("dd")) {
            Hook hook = Hooks.createFromRunelite("xd", "ClientPreferences.updateGameState()");
            Reflection.invokeMethod(hook, (Object) null, new Object[]{10, -82});
        }

        if (line.startsWith("banklast")) {
            Config.set(304, 6);
        }

        if (line.startsWith("restartproxy")) {
            ProxyManager.restartProxy(new Proxy("127.0.0.1:8000:schabowy:kotlet"));
        }

        if (line.startsWith("accstate")) {
            GrandExchangeOffer offer = new GrandExchangeOffer(new Item(314, 0), 1000, false);
            Debug.log("Offer state " + offer.getStatus());
            offer.abort();
        }

        if (line.startsWith("accstate")) {
            Debug.log("Account state " + Client.getLoginAccountState());
        }

        if (line.startsWith("shop")) {
            Debug.log("Open " + Shop.isOpen());
            Debug.log(Arrays.toString(Shop.getAll().stream().map((i) -> {
                return i.getId();
            }).toArray()));
        }

        int y;
        int position;
        if (line.startsWith("options")) {
            String[] var9 = Dialogue.getOptions();
            y = var9.length;

            for (position = 0; position < y; ++position) {
                String str = var9[position];

            }
        }

        if (line.startsWith("system")) {
            Debug.log(Reflection.getField("fh.s").get((Object) null));
        }

        if (line.startsWith("loginscr ")) {
            Client.setLoginScreenId(Integer.parseInt(line.split(" ")[1]));
        }

        String location;
        if (line.startsWith("cmd ")) {
            location = line.substring("cmd ".length());
            Debug.log(Terminal.execute(location));
        }

        if (line.equalsIgnoreCase("proxytest")) {
            WebClient wc1 = new WebClient();
            wc1.setProxy("209.205.212.34:1500");
            Debug.log(wc1.downloadString("https://oldschool.runescape.com/"));
        }

        if (line.equalsIgnoreCase("del")) {
            // GameLoader.clearCache();
        }

        int x;
        if (line.equalsIgnoreCase("free")) {
            x = 1048576;
            Runtime instance = Runtime.getRuntime();

            Debug.log("Total Memory: " + instance.totalMemory() / (long) x);
            Debug.log("Free Memory: " + instance.freeMemory() / (long) x);
            Debug.log("Used Memory: " + (instance.totalMemory() - instance.freeMemory()) / (long) x);
            Debug.log("Max Memory: " + instance.maxMemory() / (long) x);
        }

        long start;
        if (line.equalsIgnoreCase("pf")) {
            start = System.currentTimeMillis();
            WorldData.getCollisionFlags(Client.getPlane());
            Debug.log("Cache took " + (System.currentTimeMillis() - start));
        }

        if (line.equalsIgnoreCase("click")) {
            Mouse.click(1);
        }

        if (line.equalsIgnoreCase("lowmem")) {
            Client.setLowMemory(!Client.isLowMemory());
            Debug.log("Low mem: " + Client.isLowMemory());
        }

        Iterator var17;
        if (line.equalsIgnoreCase("worlds")) {
            var17 = Client.getWorldList().iterator();

            while (var17.hasNext()) {
                World w = (World) var17.next();
                Debug.log(w.getId() + " " + w.getType() + "  " + (w.isSafeF2p() ? "F2P" : "MEMBERS") + "    ");
            }


            Debug.log(String.join(", ", (CharSequence[]) Client.getF2pWorlds().stream().map((wx) -> {
                return "" + WorldHopping.toRegularWorldNumber(wx);
            }).toArray((x$0) -> {
                return new String[x$0];
            })));

            Debug.log(String.join(", ", (CharSequence[]) Client.getP2pWorlds().stream().map((wx) -> {
                return "" + WorldHopping.toRegularWorldNumber(wx);
            }).toArray((x$0) -> {
                return new String[x$0];
            })));
        }

        if (line.equalsIgnoreCase("reload")) {
            Bot.getSelectedBot().reload();
        }

        if (line.equalsIgnoreCase("logout")) {
            Login.logout();
        }

        if (line.equalsIgnoreCase("menu")) {
            for (x = 0; x < 255; ++x) {
                Menu.getOpcodes()[x] = 0;
                Menu.getVariable()[x] = 1;
                Menu.getOptions()[x] = "XDD";
                Menu.getActions()[x] = "Beniz";
                Menu.getActionNames()[x] = "hehe";
                Menu.getXInteractions()[x] = 2;
                Menu.getYInteractions()[x] = 2;
            }
        }

        if (line.equalsIgnoreCase("canvas")) {
        }

        if (line.equalsIgnoreCase("ci")) {
            Debug.log("System id: " + ComputerInfo.getInstance().getSystemId());
        }

        if (line.equalsIgnoreCase("webdata")) {
            WebData.load();
        }

        if (line.equalsIgnoreCase("apitest")) {
            Debug.log("Current preferredWorld: " + Client.getCurrentWorld());
            Debug.log("Base X: " + Client.getBaseX());
            Debug.log("Run energy: " + Client.getRunEnergy());
            Debug.log("Login state " + Client.getLoginState());
            Debug.log("Password: " + Client.getPassword());
            Debug.log("Plane: " + Client.getPlane());
        }

        if (line.equals("accurate")) {
            AttackMode.ACCURATE.enable();
        }

        if (line.equals("defensive")) {
            AttackMode.DEFENSIVE.enable();
        }

        if (line.equals("aggresive")) {
            AttackMode.AGGRESIVE.enable();
        }

        if (line.equals("eq")) {
            Widget widget = Widgets.get(387, 15, 1);
            Field[] var16 = Reflection.getFields("fb");
            position = var16.length;

            for (int var20 = 0; var20 < position; ++var20) {
                Field field = var16[var20];

                try {
                    field.setAccessible(true);
                    Object value = field.get(widget.instance);
                    Debug.log(field.getType().getName() + " " + field.getName() + " = " + value + ";");
                } catch (IllegalAccessException var7) {
                    var7.printStackTrace();
                }
            }
        }

        if (line.startsWith("npctest")) {
            NpcDefinition var21 = Npcs.get((n) -> {
                return n.getId() == 6520;
            }).getDefinition();
        }

        if (line.startsWith("spec")) {
            Combat.setSpecialAttack(true);
        }

        if (line.startsWith("roofs")) {
            Client.getGameConfig().setRoofsEnabled(!Client.getGameConfig().isRoofsEnabled());
            Debug.log("roofs active: " + Client.getGameConfig().isRoofsEnabled());
        }

        if (line.startsWith("offer x")) {
            Inventory.get(995).setWidget(Widgets.forId(22020096)).interact("Offer-X");
            InputBox.input(30000);
            Time.waitInventoryChange();
        }

        if (line.startsWith("offer all")) {
            Inventory.get(995).setWidget(Widgets.forId(22020096));
            Time.waitInventoryChange();
        }

        if (line.startsWith("zoom")) {
            x = Client.getZoom();
            y = Client.getZoomExact();


        }

        if (line.startsWith("setzoom ")) {
            x = Integer.parseInt(line.split(" ")[1]);
            Client.setZoom(x);
        }

        if (line.startsWith("withdrawx ")) {
            x = Integer.parseInt(line.split(" ")[1]);
            Config.set(304, x * 2);
        }

        if (line.startsWith("setzoomexact ")) {
            x = Integer.parseInt(line.split(" ")[1]);
        }

        String content;
        if (line.startsWith("npc ")) {
            location = line.split(" ")[1];
            content = line.substring(location.length() + 5);
            Npcs.get(location).interact(content);
        }

        if (line.startsWith("follow")) {
            location = line.split(" ")[1];
            Players.get(location).interact("Follow");
        }

        if (line.equalsIgnoreCase("login")) {
            Bot.currentThreadBot.proxyManager.setProxy();
            Login.login();
        }

        if (line.equalsIgnoreCase("ge")) {
            GrandExchange.goToMainScreen();
        }

        if (line.equalsIgnoreCase("lo")) {
            AccountData acc = new AccountData("perfectzerker@gmail.com", 301, "");
            acc.password = "freedom2020";
            acc.autostartScript = "Quester|MuleReceiver(main)";
            acc.isBanned = false;
            acc.isFlaggedAsStolen = false;
            Bot.getSelectedBot().getSession().setAccount(acc);
            Login.login();
        }

        if (line.equalsIgnoreCase("hooks")) {

            Hooks.load();

        }

        if (line.equalsIgnoreCase("objects")) {
            start = System.currentTimeMillis();
            GameObjects.getAll();
            Debug.log(System.currentTimeMillis() - start + " ms");
        }

        if (line.equalsIgnoreCase("getest")) {
            Debug.log("Active offers: " + GrandExchangeItem.getItems().length);
            GrandExchangeItem[] var26 = GrandExchangeItem.getItems();
            y = var26.length;

            for (position = 0; position < y; ++position) {
                GrandExchangeItem item = var26[position];
                Debug.log("Item ID: " + item.getId() + "Amount: " + item.getAmount() + " State:" + item.getState());
            }
        }

        if (line.equalsIgnoreCase("pm")) {
            location = "ktolet77";
            content = "Witaj kurwo pierdolona.";
            Client.getEncryptedStream1().writePacket(253);
            Client.getEncryptedStream1().writeInt8(0);
            position = Client.getEncryptedStream1().getPosition();
            Client.getEncryptedStream1().writeString(location);
            Client.getEncryptedStream1().writeEncodedString(content);
            Client.getEncryptedStream1().endPacket8(Client.getEncryptedStream1().getPosition() - position);
        }

        if (line.equalsIgnoreCase("stream")) {
            EncryptedStream stream = Client.getEncryptedStream1();
            Debug.log("Stream position: " + stream.getPosition());
        }

        if (line.equalsIgnoreCase("decline")) {
            Trade.decline();
        }

        if (line.equalsIgnoreCase("caches")) {
            HashTable table = WidgetNode.getCache();
            Debug.log("Size: " + table.getSize());
        }

        if (line.startsWith("config on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(ConfigDebug.class);
        }

        if (line.startsWith("config off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(ConfigDebug.class);
        }

        if (line.startsWith("render on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(RenderDebug.class);
        }

        if (line.startsWith("render off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(RenderDebug.class);
        }

        if (line.equalsIgnoreCase("inventory on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(InventoryDebug.class);
        }

        if (line.equalsIgnoreCase("inventory off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(InventoryDebug.class);
        }

        if (line.equalsIgnoreCase("bank on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(BankDebug.class);
        }

        if (line.equalsIgnoreCase("bank off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(BankDebug.class);
        }

        if (line.equalsIgnoreCase("dialogues on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(DialogueDebug.class);
        }

        if (line.equalsIgnoreCase("dialogues off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(DialogueDebug.class);
        }

        if (line.equalsIgnoreCase("collision on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(CollisionDebug.class);
        }

        if (line.equalsIgnoreCase("collision off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(CollisionDebug.class);
        }

        if (line.equalsIgnoreCase("menu on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(MenuDebug.class);
        }

        if (line.equalsIgnoreCase("menu off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(MenuDebug.class);
        }

        if (line.equalsIgnoreCase("widgets on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(WidgetsDebug.class);
        }

        if (line.equalsIgnoreCase("botpattern off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(BotPatternDebug.class);
        }

        if (line.equalsIgnoreCase("botpattern on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(BotPatternDebug.class);
        }

        if (line.equalsIgnoreCase("widgets off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(WidgetsDebug.class);
        }

        if (line.equalsIgnoreCase("mouse off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(MouseDebug.class);
        }

        if (line.equalsIgnoreCase("mouse on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(MouseDebug.class);
        }

        if (line.equalsIgnoreCase("gametab off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(GameTabDebug.class);
        }

        if (line.equalsIgnoreCase("gametab on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(GameTabDebug.class);
        }

        if (line.equalsIgnoreCase("objects off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(ObjectsDebug.class);
        }

        if (line.equalsIgnoreCase("objects on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(ObjectsDebug.class);
        }

        if (line.equalsIgnoreCase("npcs off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(NpcDebug.class);
        }

        if (line.equalsIgnoreCase("npcs on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(NpcDebug.class);
        }

        if (line.equalsIgnoreCase("client off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(ClientDebug.class);
        }

        if (line.equalsIgnoreCase("client on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(ClientDebug.class);
        }

        if (line.equalsIgnoreCase("player off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(PlayerDebug.class);
        }

        if (line.equalsIgnoreCase("player on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(PlayerDebug.class);
        }

        if (line.equalsIgnoreCase("path off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(PathFinderDebug.class);
        }

        if (line.equalsIgnoreCase("path on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(PathFinderDebug.class);
        }

        if (line.equalsIgnoreCase("walk off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(WalkingDebug.class);
        }

        if (line.equalsIgnoreCase("walk on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(WalkingDebug.class);
        }

        if (line.equalsIgnoreCase("grounditems off")) {
            Bot.getSelectedBot().getScriptHandler().removeDebug(GroundItemsDebug.class);
        }

        if (line.equalsIgnoreCase("grounditems on")) {
            Bot.getSelectedBot().getScriptHandler().addDebug(GroundItemsDebug.class);
        }

        if (line.equalsIgnoreCase("stopscript")) {
            Bot.getSelectedBot().getScriptHandler().stop();
        }

        if (line.startsWith("startscript")) {
            Bot.getSelectedBot().getScriptHandler().start(Bot.getSelectedBot().getScriptHandler().scriptLoader.loadWithList());
        }

        if (line.startsWith("startscript ")) {
            location = line.substring("startscript ".length());
            Bot.getSelectedBot().getScriptHandler().start(Bot.getSelectedBot().getScriptHandler().scriptLoader.load(location));
        }

        if (line.equalsIgnoreCase("restartscript")) {
            Bot.getSelectedBot().getScriptHandler().start(Bot.getSelectedBot().getScriptHandler().scriptLoader.loadLast());
        }

        if (line.equalsIgnoreCase("door")) {
            GameObjects.get((o) -> {
                return o.getName().contains("Door");
            }).interact("Open");
        }

        if (line.equalsIgnoreCase("withdraw")) {
            Debug.log("Bank " + Bank.isOpen());
            Bank.withdraw(995, 1);
        }

        if (line.equalsIgnoreCase("drop")) {
            Item[] var29 = Inventory.getAll();
            y = var29.length;

            for (position = 0; position < y; ++position) {
                Item item = var29[position];
                item.interact("Drop");
                Time.sleep(100, 300);
            }
        }

        if (line.equalsIgnoreCase("deposit1")) {
            Inventory.getAll()[1].interact("Deposit-1");
        }

        if (line.equalsIgnoreCase("deposit")) {
            Bank.depositAllExcept((i) -> {
                return i.getName().contains("lobster");
            });
        }

        if (line.equalsIgnoreCase("note")) {
            Bank.setNoted(!Bank.isNoted());
        }

        if (line.equalsIgnoreCase("openbank")) {

            Bank.openNearest();
        }

        if (line.equalsIgnoreCase("keyconfig")) {
            KeyBindingConfig.setDefaults();
        }

        if (line.equalsIgnoreCase("lowcpuf")) {
            Settings.lowCpu = !Settings.lowCpu;
            Client.setLowCpu(Settings.lowCpu);

        }

        if (line.equalsIgnoreCase("lowcpu")) {
            Settings.lowCpu = !Settings.lowCpu;

        }

        if (line.equalsIgnoreCase("closemenu")) {
            Menu.close();
        }

        if (line.equalsIgnoreCase("closetop")) {
            Widgets.closeTopInterface();
        }

        if (line.equalsIgnoreCase("widget")) {
            Widgets.getCloseButton();
        }

        if (line.startsWith("selectworld ")) {
            final int xx = Integer.parseInt(line.split(" ")[1]);
            Client.switchToWorld((World) Client.getWorlds((wx) -> {
                return wx.getId() == xx;
            }).get(0));
        }

        if (line.startsWith("hopworld ")) {
            x = Integer.parseInt(line.split(" ")[1]);
            WorldHopping.hop(x);
        }

        if (line.startsWith("walk ")) {
            x = Integer.parseInt(line.split(" ")[1]);
            y = Integer.parseInt(line.split(" ")[2]);

            Walking.walkTo(new Tile(x, y, 0));
        }

        if (line.equalsIgnoreCase("run")) {
            Walking.setRun(true);
        }

        if (line.equalsIgnoreCase("ruins")) {
            GameObjects.get(14989).interact("Enter");
        }

        if (line.equalsIgnoreCase("update")) {
            Updater.updateFiles();
        }

        if (line.equalsIgnoreCase("actioninteract")) {
            ObjectAction.create("Use", GameObjects.get(26801)).sendByMouse();
        }

        if (line.equalsIgnoreCase("setmenu")) {
            Action a = ItemAction.create(ItemMethod.DROP, Inventory.get("Logs"));
            Menu.setAllItems(a.arg1, a.arg2, a.arg3, a.arg4, a.sArg1, a.sArg2);
        }

        if (line.equalsIgnoreCase("itemoncharacter")) {
            Inventory.get("Bucket of milk").interactWith((Character) Npcs.get("Cook"));
        }

        if (line.equalsIgnoreCase("restartscripts")) {
            var17 = Main.bots.iterator();

            while (var17.hasNext()) {
                Bot bot = (Bot) var17.next();
                bot.getScriptHandler().restart();
            }
        }

        if (line.equalsIgnoreCase("deselect")) {
            Client.deselectItem();
        }

        if (line.startsWith("input1")) {
            InputBox.getTextWidget().setText("1200*");
        }

        if (line.startsWith("input ")) {
            Debug.log("Open: " + InputBox.isOpen());
            InputBox.input(10000);
        }

        if (line.equalsIgnoreCase("camera")) {
            Camera.setX(Camera.getX() + 100);
            Camera.setY(Camera.getY() + 100);
        }

        if (line.equalsIgnoreCase("fire")) {
            Inventory.get("Tinderbox").interactWith(Inventory.get("Logs"));
        }

        if (line.equalsIgnoreCase("equip")) {
            Inventory.get("Bronze axe").interact("Equip");
        }

        if (line.equalsIgnoreCase("tab") && GameTab.OPTIONS.openByClick()) {
        }

    }
}
