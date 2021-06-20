package com.farm.ibot.api.accessors.gameobject;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Model;
import com.farm.ibot.api.accessors.Renderable;
import com.farm.ibot.api.accessors.Wrapper;
import com.farm.ibot.api.accessors.interfaces.IGameObject;
import com.farm.ibot.api.data.definition.ObjectDefinition;
import com.farm.ibot.api.interact.Interact;
import com.farm.ibot.api.interfaces.Animable;
import com.farm.ibot.api.interfaces.Interactable;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.world.WorldData;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;

import java.awt.*;
import java.util.ArrayList;

public class GameObject extends Wrapper implements Animable, Interactable {
    public GameObject(Object instance) {
        super(instance);
    }

    public static IGameObject getGameObjectInterface() {
        return Bot.get().accessorInterface.gameObjectReflectionInterface;
    }

    @HookName("GameObject.Uid")
    public long getUid() {
        return getGameObjectInterface().getUid(this.instance);
    }

    @HookName("GameObject.AnimableX")
    public int getAnimableX() {
        return getGameObjectInterface().getAnimableX(this.instance);
    }

    @HookName("GameObject.AnimableY")
    public int getAnimableY() {
        return getGameObjectInterface().getAnimableY(this.instance);
    }

    @HookName("GameObject.Renderable")
    public Renderable getRenderable() {
        return getGameObjectInterface().getRenderable(this.instance);
    }

    @HookName("GameObject.Renderable")
    public void setRenderable(Object value) {
        this.set("GameObject.Renderable", value);
    }

    @HookName("GameObject.Orientation")
    public int getOrientation() {
        return getGameObjectInterface().getOrientation(this.instance);
    }

    @HookName("GameObject.Id")
    public int getRealId() {
        return (Integer) this.get("GameObject.Id");
    }

    public int getModelHeight() {
        return this.getRenderable().getModelHeight();
    }

    public int getId() {
        long id = this.getUid() >>> 17 & 4294967295L;
        if (id > 26800L && id < 26840L) {
            id -= 14L;
        }

        return (int) id;
    }

    public int getIdUnsafe() {
        long id = this.getUid() >>> 17 & 4294967295L;
        return (int) id;
    }

    public ObjectDefinition getDefinition() {
        return ObjectDefinition.forId(this.getId());
    }

    public String getName() {
        return this.getDefinition().name;
    }

    public String[] getActions() {
        return this.getDefinition().actions;
    }

    public boolean interact(String action) {
        return Interact.interactHandler.objectInteract(action, this);
    }

    public boolean interactAndWaitDisappear(String action) {
        boolean interact = this.interact(action);
        Time.sleep(1000, 1600);
        return interact;
    }

    public boolean exists() {
        return GameObjects.getAt(this.getPosition()).stream().anyMatch((o) -> {
            return o.getId() == this.getId() && o.getUid() == this.getUid();
        });
    }

    public boolean isInteractable() {
        String[] var1 = this.getActions();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            String action = var1[var3];
            if (action != null && action.length() > 1 && !action.equals("null")) {
                return true;
            }
        }

        return false;
    }

    public boolean isReachable() {
        for (int x = -((int) Math.ceil((double) this.getDefinition().width / 2.0D)); x <= (int) Math.ceil((double) this.getDefinition().width / 2.0D); ++x) {
            for (int y = -((int) Math.ceil((double) this.getDefinition().height / 2.0D)); y <= (int) Math.ceil((double) this.getDefinition().height / 2.0D); ++y) {
                Tile tile = this.getPosition().add(x, y);
                if (tile.isReachable() && WorldData.getCollisionFlag(tile) == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public Tile getNearestFrontTile() {
        Tile nearest = null;
        int distance = 150;

        for (int x = -((int) Math.ceil((double) this.getDefinition().width / 2.0D)); x <= (int) Math.ceil((double) this.getDefinition().width / 2.0D); ++x) {
            for (int y = -((int) Math.ceil((double) this.getDefinition().height / 2.0D)); y <= (int) Math.ceil((double) this.getDefinition().height / 2.0D); ++y) {
                Tile tile = this.getPosition().add(x, y);
                int realDist = tile.realDistance();
                if (WorldData.getCollisionFlag(tile) == 0 && realDist < distance) {
                    nearest = tile;
                    distance = tile.distance();
                }
            }
        }

        return nearest;
    }

    public Model getModel() {
        try {
            Model m = new Model(this.getRenderable().instance);
            m.getIndicesLength();
            return m;
        } catch (Exception var2) {
            return null;
        }
    }

    public Polygon getConvexHull() {
        Model model = this.getModel();
        if (model != null) {
            int tileHeight = Screen.getTileHeight(Client.getPlane(), this.getPosition().toLocalTile().getX(), this.getPosition().toLocalTile().getY());
            return this.getModel().getConvexHull(this.getAnimableX(), this.getAnimableY(), this.getOrientation(), tileHeight);
        } else {
            return null;
        }
    }

    public ArrayList<Point> getConvexHullPoints() {
        Model model = this.getModel();
        if (model != null) {
            int tileHeight = Screen.getTileHeight(Client.getPlane(), this.getPosition().toLocalTile().getX(), this.getPosition().toLocalTile().getY());
            return this.getModel().getConvexHullPoints(this.getAnimableX(), this.getAnimableY(), this.getOrientation(), tileHeight);
        } else {
            return null;
        }
    }
}
