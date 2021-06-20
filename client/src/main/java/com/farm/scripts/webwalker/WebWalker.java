package com.farm.scripts.webwalker;

import com.farm.ibot.Main;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.accessors.gameobject.GameObject;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.Dialogue;
import com.farm.ibot.api.methods.entities.GameObjects;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.methods.walking.WebWalkingCache;
import com.farm.ibot.api.util.PaintUtils;
import com.farm.ibot.api.world.pathfinding.impl.WebPathFinder;
import com.farm.ibot.api.world.webwalking.node.ObjectWebNode;
import com.farm.ibot.api.world.webwalking.node.WebNode;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.BotScript;

import javax.swing.*;
import java.awt.*;

public class WebWalker extends BotScript implements PaintHandler {
    private Tile destination;
    private Tile nextTile;
    private GameObject gameObject;

    public void onStart() {
        String[] coords = this.startArguments.split(",");
        if (coords.length <= 1) {
            coords = JOptionPane.showInputDialog(Main.frame, "Where to walk? Eg. 3200,3200,1  \n (3208, 3218, 2) - Lumbridge Castle \n (3253, 3423, 0) - Varrock East \n (3213, 3428, 0) - Varrock Fountain \n (3170, 3484, 0) - Grand Exchange", "3170,3484,0").split(",");
        }

        int x = 0;
        int y = 0;
        int z = 0;
        if (coords.length > 1) {
            x = Integer.parseInt(coords[0]);
            y = Integer.parseInt(coords[1]);
        }

        if (coords.length > 2) {
            z = Integer.parseInt(coords[2]);
        }

        this.destination = new Tile(x, y, z);
    }

    public int onLoop() {

        if (!Dialogue.contains("Greetings Adventurer") && !Dialogue.contains("Please pass through")) {
            Widget enterNextLevelWidget = Widgets.get((w) -> {
                return w.getText().contains("Yes - I know that it may be");
            });
            if (enterNextLevelWidget != null) {
                enterNextLevelWidget.interact("Yes");
                return 3000;
            } else {
                this.nextTile = WebWalking.getBestTile((Tile[]) (new WebPathFinder()).findPath(Player.getLocal().getPosition(), Player.getLocal().getPosition()).toArray(new WebNode[0]));
                if (this.nextTile instanceof ObjectWebNode) {
                    ObjectWebNode n = (ObjectWebNode) this.nextTile;
                    this.gameObject = n.objectId == -1 ? GameObjects.get(n.objectName, n) : GameObjects.get(n.objectId, n);
                } else {
                    this.gameObject = null;
                }


                if (WebWalking.walkTo(this.destination, 5, new Tile[0])) {

                }


                return 1;
            }
        } else {
            Dialogue.clickContinue();
            return 1000;
        }
    }

    public void onPaint(Graphics g) {
        g.setColor(Color.white);
        this.drawString(g, "Web pathes: " + WebWalkingCache.pathCache.size());
        if (this.nextTile != null) {
            g.setColor(Color.yellow);
            PaintUtils.drawTile(g, this.nextTile, this.nextTile.toString());
        }

        if (this.gameObject != null) {
            g.setColor(Color.orange);
            PaintUtils.fillTile(g, this.gameObject.getPosition());
            this.drawString(g, "DIST: " + this.gameObject.getPosition().distance(this.nextTile));
        }

    }
}
