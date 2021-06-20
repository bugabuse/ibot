package com.farm.scripts.humanfletcher.api.action;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Keyboard;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Debug;
import com.farm.ibot.api.util.SeedRandom;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.item.Item;
import com.farm.ibot.api.wrapper.item.container.ItemContainer;
import com.farm.ibot.init.AccountData;
import com.farm.ibot.init.Settings;
import com.farm.scripts.humanfletcher.api.action.wrapper.*;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ActionManager {
    private ArrayList<ActionContainer> allActions = new ArrayList();
    private ArrayList<ActionContainer> actions = new ArrayList();
    private RsRobot robot = new RsRobot();
    private ActionContainer lastContainer;

    public ArrayList<ActionContainer> load(String fileName) {
        Random rnd = new Random(AccountData.seedForCurrentDay());
        List lines = null;

        try {
            lines = Files.readAllLines(Settings.getFile(fileName).toPath());
        } catch (IOException var14) {
            var14.printStackTrace();
        }

        int index = -1;
        ActionContainer current = new ActionContainer();
        Iterator var6 = lines.iterator();

        while (true) {
            while (var6.hasNext()) {
                String line = (String) var6.next();
                if (line.contains("[BEGIN]")) {
                    ++index;
                    current = new ActionContainer();
                    current.id = index;
                } else if (line.contains("[END]")) {
                    if (current != null) {
                        int maxRandom = 7;
                        if (current.isIdling || current.isReaction) {
                            maxRandom = 2;
                        }

                        if (rnd.nextInt(maxRandom) == 1) {
                            this.actions.add(current);
                        }

                        this.allActions.add(current);
                        current = null;
                    }
                } else if (line.contains("[IDLE_WORKING]")) {
                    current.isIdling = true;
                } else if (line.contains("[REACTION]")) {
                    current.isReaction = true;
                } else {
                    String[] args = line.split("\\|");
                    Action action = null;
                    if (line.startsWith("move")) {
                        action = new MouseEventAction(Long.parseLong(args[1]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[2]), true);
                    } else if (line.startsWith("click")) {
                        action = new MouseEventAction(Long.parseLong(args[1]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[2]), true);
                    } else if (line.startsWith("key")) {
                        action = new KeyAction(Long.parseLong(args[1]), args[0], Integer.parseInt(args[2]));
                    } else if (line.startsWith("game_object")) {
                        action = new GameObjectAction(args[3], Long.parseLong(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3].split(",")[0]), Integer.parseInt(args[3].split(",")[1]));
                    } else if (line.startsWith("reaction")) {
                        action = new ReactionAction(Long.parseLong(args[1]));
                    } else if (line.startsWith("item") || line.startsWith("widget") || line.startsWith("pre_widget")) {
                        int x = Integer.parseInt(args[4].split(",")[0]);
                        int y = Integer.parseInt(args[4].split(",")[1]);
                        int width = Integer.parseInt(args[4].split(",")[2]);
                        int height = Integer.parseInt(args[4].split(",")[3]);
                        if (line.startsWith("item")) {
                            action = new ItemAction(args[3], Long.parseLong(args[1]), Integer.parseInt(args[2]), x, y, width, height);
                        } else if (line.startsWith("widget")) {
                            action = new WidgetAction(args[3], Long.parseLong(args[1]), Integer.parseInt(args[2]), x, y, width, height);
                        } else if (line.startsWith("pre_widget")) {
                            action = new PreWidgetAction(args[3], Long.parseLong(args[1]), Integer.parseInt(args[2]), x, y, width, height);
                        }
                    }

                    if (action != null) {
                        current.getActions().add(action);
                    } else {

                    }
                }
            }

            int removed = 0;
            Iterator var16 = (new ArrayList(this.actions)).iterator();

            while (var16.hasNext()) {
                ActionContainer action = (ActionContainer) var16.next();
                if (action.isReaction && action.getActions().size() < 2) {
                    this.actions.remove(action);
                    ++removed;
                }
            }


            Debug.log("We have " + this.actions.stream().filter((a) -> {
                return a.isReaction;
            }).count() + " reaction actions there.");
            Debug.log("Loaded " + this.actions.size() + " unique human actions!");
            return this.actions;
        }
    }

    private ActionContainer find(Class type, Predicate<Action> predicate) {
        ActionContainer ourActions = this.find(this.actions, type, predicate);
        return ourActions != null ? ourActions : null;
    }

    private ActionContainer find(ArrayList<ActionContainer> containerList, Class type, Predicate<Action> predicate) {
        ArrayList<ActionContainer> availableContainers = new ArrayList();
        Iterator var5 = containerList.iterator();

        while (true) {
            ActionContainer container;
            Action action;
            do {
                do {
                    do {
                        if (!var5.hasNext()) {
                            if (availableContainers.size() > 0) {
                                return (ActionContainer) availableContainers.get(com.farm.ibot.api.util.Random.next(0, availableContainers.size()));
                            }

                            return null;
                        }

                        container = (ActionContainer) var5.next();
                    } while (container == null);
                } while (container.getActions() == null);

                action = container.getMainAction();
                if (type == ReactionAction.class) {
                    action = (Action) container.getActions().get(0);
                }
            } while (type != null && action.getClass() != type);

            if (predicate.test(action)) {
                availableContainers.add(container);
            }
        }
    }

    private ActionContainer findWhichContains(ArrayList<ActionContainer> containerList, Class type, Predicate<Action> predicate) {
        ArrayList<ActionContainer> availableContainers = new ArrayList();
        Iterator var5 = containerList.iterator();

        while (true) {
            label44:
            while (true) {
                ActionContainer container;
                do {
                    do {
                        if (!var5.hasNext()) {
                            if (availableContainers.size() > 0) {
                                return (ActionContainer) availableContainers.get(com.farm.ibot.api.util.Random.next(0, availableContainers.size()));
                            }

                            return null;
                        }

                        container = (ActionContainer) var5.next();
                    } while (container == null);
                } while (container.getActions() == null);

                Iterator var7 = container.getActions().iterator();

                Action action;
                do {
                    do {
                        if (!var7.hasNext()) {
                            continue label44;
                        }

                        action = (Action) var7.next();
                    } while (type != null && action.getClass() != type);
                } while (!predicate.test(action));

                availableContainers.add(container);
            }
        }
    }

    public ActionContainer findIdlingAction() {
        ActionContainer[] containers = (ActionContainer[]) this.actions.stream().filter((container) -> {
            return container.isIdling;
        }).toArray((x$0) -> {
            return new ActionContainer[x$0];
        });
        return containers[com.farm.ibot.api.util.Random.next(0, containers.length)];
    }

    public ActionContainer findKeyAction(int keyCode) {
        return this.find(KeyAction.class, (a) -> {
            KeyAction action = (KeyAction) a;
            return action.key == keyCode;
        });
    }

    public ActionContainer findReactionAction() {
        return this.find(ReactionAction.class, (a) -> {
            return true;
        });
    }

    public ActionContainer findGameObjectAction(int objectId, String menuOption) {
        return this.find(GameObjectAction.class, (a) -> {
            GameObjectAction action = (GameObjectAction) a;
            return action.objectId == objectId && action.menuOption.contains(menuOption);
        });
    }

    public ActionContainer findWidgetAction(int widgetId, String menuOption) {
        return this.findWidgetAction(widgetId, menuOption, true);
    }

    public ActionContainer findWidgetAction(int widgetId, String menuOption, boolean checkValid) {
        Widget widget = Widgets.forId(widgetId);
        return !checkValid || widget != null && !widget.isHidden() ? this.find(WidgetAction.class, (a) -> {
            WidgetAction action = (WidgetAction) a;
            return action.widgetId == widgetId && action.menuOption.contains(menuOption);
        }) : null;
    }

    public ActionContainer findWidgetOrKeyAction(int widgetId, String menuOption, int keyCode) {
        return this.findWidgetOrKeyAction(widgetId, menuOption, keyCode, true);
    }

    public ActionContainer findWidgetOrKeyAction(int widgetId, String menuOption, int keyCode, boolean checkValid) {
        Widget widget = Widgets.forId(widgetId);
        return !checkValid || widget != null && !widget.isHidden() ? this.find((Class) null, (a) -> {
            if (a.getClass() == WidgetAction.class) {
                if (!a.menuOption.contains(menuOption)) {
                    return false;
                } else {
                    WidgetAction actionx = (WidgetAction) a;
                    return actionx.widgetId == widgetId;
                }
            } else if (a.getClass() == KeyAction.class) {
                KeyAction action = (KeyAction) a;

                return action.key == keyCode;
            } else {
                return false;
            }
        }) : null;
    }

    public ActionContainer[] findItemOnItemAction(int widgetId, int itemId1, int itemId2) {
        Widget widget = Widgets.forId(widgetId);
        if (widget != null && !widget.isHidden()) {
            ItemContainer itemContainer1 = new ItemContainer(widget);
            ItemContainer itemContainer2 = new ItemContainer(widget.getChildren());
            ItemContainer itemContainer;
            if (itemContainer1.getItems().length > 0) {
                itemContainer = itemContainer1;
            } else {
                itemContainer = itemContainer2;
            }

            if (itemContainer.getItems().length <= 0) {
                return null;
            } else {
                ActionContainer action1 = this.find(ItemAction.class, (a) -> {
                    ItemAction action = (ItemAction) a;
                    if (action.widgetId != widgetId) {
                        return false;
                    } else {
                        Item[] var5 = itemContainer.getItems();
                        int var6 = var5.length;

                        for (int var7 = 0; var7 < var6; ++var7) {
                            Item widgetItem = var5[var7];
                            if (widgetItem.getId() == itemId1 && widgetItem.getBounds().contains(action.x, action.y, action.width, action.height)) {
                                return true;
                            }
                        }

                        return false;
                    }
                });
                if (action1 == null) {
                    return null;
                } else {
                    long action1Time = ((Action) action1.getActions().get(action1.getActions().size() - 1)).time;
                    ActionContainer action2 = this.find(this.allActions, ItemAction.class, (a) -> {
                        ItemAction action = (ItemAction) a;
                        if (action.widgetId != widgetId) {
                            return false;
                        } else {
                            Item[] var7 = itemContainer.getItems();
                            int var8 = var7.length;

                            for (int var9 = 0; var9 < var8; ++var9) {
                                Item widgetItem = var7[var9];
                                if (Math.abs(action1Time - action.time) < 5000L && widgetItem.getId() == itemId2 && widgetItem.getBounds().contains(action.x, action.y, action.width, action.height)) {
                                    return true;
                                }
                            }

                            return false;
                        }
                    });
                    if (action2 == null) {

                        return null;
                    } else {
                        long action2Time = ((Action) action2.getActions().get(action2.getActions().size() - 1)).time;
                        return action1Time < action2Time ? new ActionContainer[]{action1, action2} : new ActionContainer[]{action2, action1};
                    }
                }
            }
        } else {
            return null;
        }
    }

    public ActionContainer findItemAction(int widgetId, int itemId) {
        return this.findItemAction(widgetId, itemId, false);
    }

    public ActionContainer findItemAction(int widgetId, int itemId, boolean validate) {
        Widget widget = Widgets.forId(widgetId);
        if (!validate || widget != null && !widget.isHidden()) {
            ItemContainer itemContainer = new ItemContainer(widget);
            ItemContainer itemContainer2 = new ItemContainer(widget.getChildren());
            return this.find(ItemAction.class, (a) -> {
                ItemAction action = (ItemAction) a;
                if (action.widgetId == widgetId) {
                    Item[] var6;
                    int var7;
                    int var8;
                    Item widgetItem;
                    if (itemContainer.getItems().length > 0) {
                        var6 = itemContainer.getItems();
                        var7 = var6.length;

                        for (var8 = 0; var8 < var7; ++var8) {
                            widgetItem = var6[var8];
                            if (widgetItem.getId() == itemId && widgetItem.getBounds().contains(action.x, action.y, action.width, action.height)) {
                                return true;
                            }
                        }
                    }

                    if (itemContainer2.getItems().length > 0) {
                        var6 = itemContainer2.getItems();
                        var7 = var6.length;

                        for (var8 = 0; var8 < var7; ++var8) {
                            widgetItem = var6[var8];
                            if (widgetItem.getId() == itemId && widgetItem.getBounds().contains(action.x, action.y, action.width, action.height)) {
                                return true;
                            }
                        }
                    }
                }

                return false;
            });
        } else {
            return null;
        }
    }

    public void translateRectangle(Rectangle rectangle, Rectangle destination) {
        int diffX = destination.x - rectangle.x;
        int diffY = destination.y - rectangle.y;
        Iterator var5 = this.actions.iterator();

        while (true) {
            while (var5.hasNext()) {
                ActionContainer container = (ActionContainer) var5.next();
                Iterator var7 = container.getActions().iterator();

                while (var7.hasNext()) {
                    Action action = (Action) var7.next();
                    if (action instanceof WidgetAction) {
                        WidgetAction wAction = (WidgetAction) action;
                        if ((new Rectangle(wAction.x, wAction.y, wAction.width, wAction.height)).equals(rectangle)) {
                            wAction.x += diffX;
                            wAction.y += diffY;
                            Debug.log("Translated to " + new Rectangle(wAction.x, wAction.y, wAction.width, wAction.height) + " from " + destination);
                            Iterator var10 = container.getActions().iterator();

                            while (var10.hasNext()) {
                                Action a = (Action) var10.next();
                                if (a instanceof MouseEventAction) {
                                    ((MouseEventAction) a).x += diffX;
                                    ((MouseEventAction) a).y += diffY;
                                }
                            }
                            break;
                        }
                    }
                }
            }

            return;
        }
    }

    public void executeNextPreAtion() {

        if (this.lastContainer == null) {

        } else {
            ActionContainer container1 = this.findWhichContains(this.allActions, PreWidgetAction.class, (container) -> {
                return container.time > this.lastContainer.getMainAction().time && container.time - this.lastContainer.getMainAction().time < 4000L;
            });
            if (container1 != null) {

                container1 = container1.splitBefore(PreWidgetAction.class);
            }

            if (container1 != null && container1.getActions().size() > 2) {
                this.executeMouseMovementOnly(container1);
            }

        }
    }

    public void executeMouseMovementOnly(ActionContainer container) {
        MouseEventAction toExecute = null;

        Action action;
        for (Iterator var3 = container.getActions().iterator(); var3.hasNext(); toExecute = (MouseEventAction) action) {
            action = (Action) var3.next();
            if (!(action instanceof MouseEventAction) || ((MouseEventAction) action).button != 0) {
                break;
            }
        }

        if (toExecute != null) {
            Mouse.naturalMove(toExecute.x + com.farm.ibot.api.util.Random.next(-5, 5), toExecute.y + com.farm.ibot.api.util.Random.next(-5, 5));
        }

    }

    public void execute(ActionContainer container) {
        this.lastContainer = container;
        Action lastAction = null;
        KeyAction lastKeyAction = null;
        boolean startPointDetermined = false;
        if (container == null) {

        } else {
            Debug.log("Executing container: " + container.id + "(" + container.getMainAction().time + ")");

            Action action;
            for (Iterator var5 = container.getActions().iterator(); var5.hasNext(); lastAction = action) {
                action = (Action) var5.next();
                if (action instanceof PreWidgetAction && lastAction instanceof MouseEventAction) {
                    Point point = new Point(((MouseEventAction) lastAction).x + com.farm.ibot.api.util.Random.next(-5, 5), ((MouseEventAction) lastAction).y + com.farm.ibot.api.util.Random.next(-5, 5));
                    Mouse.naturalMove(point.x, point.y);
                }

                if ((!(action instanceof MouseEventAction) || !(lastAction instanceof MouseEventAction)) && lastAction != null) {
                    double time = (double) (action.time - lastAction.time) * com.farm.ibot.api.util.Random.next(0.8D, 1.2D);

                    Time.sleep(time);
                }

                if (action instanceof KeyAction) {
                    KeyAction keyAction = (KeyAction) action;

                    if (keyAction.type == KeyAction.Type.PRESS) {
                        Keyboard.press(keyAction.key);
                    }

                    if (keyAction.type == KeyAction.Type.TYPE) {
                        if (lastKeyAction != null) {
                            Time.sleep((double) (action.time - lastAction.time) * com.farm.ibot.api.util.Random.next(0.8D, 1.2D));
                        }

                        Keyboard.type(keyAction.key);
                    }

                    if (keyAction.type == KeyAction.Type.RELEASE) {
                        if (lastKeyAction != null) {
                            Time.sleep((double) (action.time - lastAction.time) * com.farm.ibot.api.util.Random.next(0.8D, 1.2D));
                        }

                        Keyboard.release(keyAction.key);
                    }

                    lastKeyAction = keyAction;
                }

                if (action instanceof MouseEventAction) {
                    MouseEventAction mouseAction = (MouseEventAction) action;
                    if (!startPointDetermined) {
                        startPointDetermined = true;
                    }

                    if (mouseAction.button != 0) {
                        boolean alreadyHovered = false;
                        Point point = new Point(mouseAction.x + com.farm.ibot.api.util.Random.next(-5, 5), mouseAction.y + com.farm.ibot.api.util.Random.next(-5, 5));
                        if (container.getMainAction() instanceof GameObjectAction) {
                            GameObjectAction gameObjectAction = (GameObjectAction) container.getMainAction();
                            Polygon poly = GameObjects.get(gameObjectAction.objectId).getConvexHull();
                            if (gameObjectAction.objectId == 10060) {
                                poly = GameObjects.combinedHull((o) -> {
                                    return o.getId() == 10060 && o.getPosition().distance() < 2;
                                });
                            }

                            point = new Point(this.randomPoint(poly));
                        }

                        if (container.getMainAction() instanceof RectangleAction) {
                            RectangleAction action1 = (RectangleAction) container.getActions().get(container.getActions().size() - 1);
                            if (action1.getRectangle().contains(Mouse.getLocation())) {
                                alreadyHovered = true;
                            }
                        }

                        if (!alreadyHovered) {
                            Mouse.naturalMove(point.x, point.y);
                        }

                        if (lastAction != null) {
                            Debug.log("Sleep: " + (action.time - lastAction.time));
                            Time.sleep((double) (action.time - lastAction.time) * com.farm.ibot.api.util.Random.next(0.8D, 1.2D));
                        }

                        Mouse.click(mouseAction.button);
                    } else {
                        Point point = new Point(mouseAction.x, mouseAction.y);
                        if (Mouse.getLocation().distance(point) < 100.0D && com.farm.ibot.api.util.Random.next(0, 400) == 0) {
                            Mouse.naturalMove(point.x, point.y);
                        }
                    }
                }
            }


            if (lastAction instanceof MouseEventAction && container.isIdling && this.isOffScreen((MouseEventAction) lastAction)) {
                Point point = this.toOffScreenPoint((MouseEventAction) lastAction);
                Mouse.naturalMove(point.x, point.y);
                this.robot.loseFocus();
            }


        }
    }

    private boolean isOffScreen(MouseEventAction mouseAction) {
        return mouseAction.x < 15 || mouseAction.x > Screen.GAME_SCREEN.width - 15 || mouseAction.y < 15 || mouseAction.y > Screen.GAME_SCREEN.height - 15;
    }

    private Point toOffScreenPoint(MouseEventAction mouseAction) {
        if (mouseAction.x < 15) {
            return new Point(mouseAction.x - 50, mouseAction.y);
        } else if (mouseAction.x > Screen.GAME_SCREEN.width - 15) {
            return new Point(mouseAction.x + 50, mouseAction.y);
        } else if (mouseAction.y < 15) {
            return new Point(mouseAction.x, mouseAction.y - 50);
        } else {
            return mouseAction.y > Screen.GAME_SCREEN.height - 15 ? new Point(mouseAction.x, mouseAction.y + 50) : new Point(mouseAction.x, mouseAction.y);
        }
    }

    private Point randomPoint(Polygon polygon) {
        ArrayList<Point> points = this.shufflePolygon(polygon);
        return (Point) points.get(com.farm.ibot.api.util.Random.next(0, points.size()));
    }

    private ArrayList<Point> shufflePolygon(Polygon polygon) {
        long hash = AccountData.seedForCurrentDay();
        Rectangle rect = polygon.getBounds();
        SeedRandom rnd = new SeedRandom(hash);
        ArrayList<Point> rootClicks = new ArrayList();
        int i = 0;

        Point toRandomize;
        while (i < 100) {
            toRandomize = new Point(rect.x + rnd.nextGaussian(0, rect.width), rect.y + rnd.nextGaussian(0, rect.height));
            if (polygon.contains(toRandomize)) {
                rootClicks.add(toRandomize);
                ++i;
            }
        }

        for (i = 0; i < 300; ++i) {
            toRandomize = (Point) rootClicks.get(rnd.next(0, rootClicks.size()));
            rootClicks.add(new Point(toRandomize.x + rnd.next(-40, 40), toRandomize.y + rnd.next(-40, 40)));
        }

        for (i = 0; i < 2000; ++i) {
            toRandomize = (Point) rootClicks.get(rnd.next(0, rootClicks.size()));
            rootClicks.add(new Point(toRandomize.x + com.farm.ibot.api.util.Random.next(-6, 6), toRandomize.y + rnd.next(-6, 6)));
        }

        return rootClicks;
    }
}
