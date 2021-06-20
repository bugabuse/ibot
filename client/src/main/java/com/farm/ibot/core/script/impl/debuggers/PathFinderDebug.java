package com.farm.ibot.core.script.impl.debuggers;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.world.pathfinding.PathFinder;
import com.farm.ibot.api.world.pathfinding.impl.LocalPathFinder;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.BackgroundScript;

import java.awt.*;

public class PathFinderDebug extends BackgroundScript implements PaintHandler {
    Tile[] tiles = null;
    PathFinder pathFinder = new LocalPathFinder(true);

    public void onStart() {
    }

    public int onLoop() {
        GameObject normal = (GameObject) GameObjects.normal.get("Bank booth");
        this.tiles = this.pathFinder.findPath(Player.getLocal().getPosition(), normal.getPosition());
        return 600;
    }

    public void onPaint(Graphics g) {
        if (this.tiles != null) {
            Tile[] var2 = this.tiles;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Tile tile = var2[var4];
                g.drawPolygon(tile.getBounds());
            }
        } else {
            this.drawString(g, Color.red, "Path is null!");
        }

        GameObject bestBank = (GameObject) GameObjects.best.get("Bank booth");
        GameObject normal = (GameObject) GameObjects.normal.get("Bank booth");
        if (bestBank != null) {
            g.drawString("Best", bestBank.getScreenPoint().x, bestBank.getScreenPoint().y);
            g.drawPolygon(bestBank.getPosition().getBounds());
        }

        if (normal != null) {
            g.drawString("Normal", normal.getScreenPoint().x, normal.getScreenPoint().y);
            g.drawPolygon(normal.getPosition().getBounds());
        }

    }
}
