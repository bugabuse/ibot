package com.farm.scripts.webmaker;

import com.farm.ibot.Main;
import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.PaintUtils;
import com.farm.ibot.api.world.pathfinding.impl.WebPathFinder;
import com.farm.ibot.api.world.webwalking.WebData;
import com.farm.ibot.api.world.webwalking.node.NpcWebNode;
import com.farm.ibot.api.world.webwalking.node.ObjectWebNode;
import com.farm.ibot.api.world.webwalking.node.StairsWebNode;
import com.farm.ibot.api.world.webwalking.node.WebNode;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.BotScript;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;

public class WebMaker extends BotScript implements PaintHandler, MouseListener {
    private final Rectangle ADD_NODE = new Rectangle(8, 347, 70, 30);
    private final Rectangle ADD_NODE_OBJECT = new Rectangle(8, 380, 70, 30);
    private final Rectangle ADD_NODE_NPC = new Rectangle(8, 460, 70, 30);
    private final Rectangle ADD_NODE_STAIRS = new Rectangle(80, 460, 70, 30);
    private final Rectangle REMOVE_NODE = new Rectangle(80, 347, 70, 30);
    private final Rectangle REMOVE_DISTANCED_CONNECTIONS = new Rectangle(80, 380, 70, 30);
    private final Rectangle REMOVE_DUPLICATED_NODES = new Rectangle(160, 380, 70, 30);
    private final Rectangle CONNECT_WITH = new Rectangle(160, 347, 70, 30);
    private final Rectangle EXPORT = new Rectangle(240, 347, 70, 30);
    private final Rectangle LOAD = new Rectangle(320, 347, 70, 30);
    private final Rectangle PATH_TEST = new Rectangle(400, 347, 70, 30);
    boolean drawArrows = false;
    private WebNode current;
    private boolean connectingBoth = false;
    private WebNode[] testPath;
    private Tile testTile = new Tile(3170, 3484);
    private Tile bestTile;
    private Tile[] testLocalPath;

    public void onStart() {
        this.addMouseListeners();
        Bot.currentThreadBot.getScriptHandler().webNotFoundRandom.nextCheck = Long.MAX_VALUE;
        Bot.currentThreadBot.getScriptHandler().dialogues.nextCheck = Long.MAX_VALUE;
    }

    public int onLoop() {
        if (this.testPath != null) {
            this.bestTile = WebWalking.getBestTile(this.testPath);
        }

        return 1500;
    }

    public void onPaint(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.drawString(g, "Nodes: " + WebData.getNodes().size());
        g.drawRect(this.ADD_NODE.x, this.ADD_NODE.y, this.ADD_NODE.width, this.ADD_NODE.height);
        g.drawString("Add", this.ADD_NODE.x + 20, this.ADD_NODE.y + 20);
        g.drawRect(this.REMOVE_DUPLICATED_NODES.x, this.REMOVE_DUPLICATED_NODES.y, this.REMOVE_DUPLICATED_NODES.width, this.REMOVE_DUPLICATED_NODES.height);
        g.drawString("Remove duplicates", this.REMOVE_DUPLICATED_NODES.x + 20, this.REMOVE_DUPLICATED_NODES.y + 20);
        g.drawRect(this.ADD_NODE_OBJECT.x, this.ADD_NODE_OBJECT.y, this.ADD_NODE_OBJECT.width, this.ADD_NODE_OBJECT.height);
        g.drawString("Add Object", this.ADD_NODE_OBJECT.x + 20, this.ADD_NODE_OBJECT.y + 20);
        g.drawRect(this.ADD_NODE_NPC.x, this.ADD_NODE_NPC.y, this.ADD_NODE_NPC.width, this.ADD_NODE_NPC.height);
        g.drawString("Add NPC", this.ADD_NODE_NPC.x + 20, this.ADD_NODE_NPC.y + 20);
        g.drawRect(this.ADD_NODE_STAIRS.x, this.ADD_NODE_STAIRS.y, this.ADD_NODE_STAIRS.width, this.ADD_NODE_STAIRS.height);
        g.drawString("Add Stairs", this.ADD_NODE_STAIRS.x + 20, this.ADD_NODE_STAIRS.y + 20);
        g.drawRect(this.REMOVE_NODE.x, this.REMOVE_NODE.y, this.REMOVE_NODE.width, this.REMOVE_NODE.height);
        g.drawString("Remove", this.REMOVE_NODE.x + 10, this.REMOVE_NODE.y + 20);
        g.drawRect(this.REMOVE_DISTANCED_CONNECTIONS.x, this.REMOVE_DISTANCED_CONNECTIONS.y, this.REMOVE_DISTANCED_CONNECTIONS.width, this.REMOVE_DISTANCED_CONNECTIONS.height);
        g.drawString("Remove distanced", this.REMOVE_DISTANCED_CONNECTIONS.x + 10, this.REMOVE_DISTANCED_CONNECTIONS.y + 20);
        g.drawRect(this.CONNECT_WITH.x, this.CONNECT_WITH.y, this.CONNECT_WITH.width, this.CONNECT_WITH.height);
        g.drawString("Connect With", this.CONNECT_WITH.x, this.CONNECT_WITH.y + 20);
        g.drawRect(this.EXPORT.x, this.EXPORT.y, this.EXPORT.width, this.EXPORT.height);
        g.drawString("Export", this.EXPORT.x, this.EXPORT.y + 20);
        g.drawRect(this.LOAD.x, this.LOAD.y, this.LOAD.width, this.LOAD.height);
        g.drawString("Load", this.LOAD.x, this.LOAD.y + 20);
        g.drawRect(this.PATH_TEST.x, this.PATH_TEST.y, this.PATH_TEST.width, this.PATH_TEST.height);
        g.drawString("PathFinding Test", this.PATH_TEST.x, this.PATH_TEST.y + 20);
        if (this.connectingBoth) {
            g.drawString("Connect with...", Mouse.getLocation().x, Mouse.getLocation().y);
        }

        Iterator var2 = WebData.getNodes().iterator();

        while (true) {
            WebNode node;
            Point screenPoint;
            //WebNode node;
            Point p;
            do {
                if (!var2.hasNext()) {
                    var2 = WebData.getNodes().iterator();

                    while (var2.hasNext()) {
                        node = (WebNode) var2.next();
                        if (node.getZ() == Client.getPlane()) {
                            screenPoint = node.getMinimapPoint();
                            if (node.equals(this.current)) {
                                g.setColor(Color.green);
                                g.drawString("" + node.getId(), screenPoint.x, screenPoint.y);
                            } else {
                                g.setColor(new Color(255, 130, 0, 150));
                            }

                            g.fillOval(screenPoint.x - 3, screenPoint.y - 3, 6, 6);
                            if (node instanceof ObjectWebNode) {
                                g.drawString("" + ((ObjectWebNode) node).objectName, screenPoint.x, screenPoint.y);
                                if (node.getZ() == Client.getPlane()) {
                                    screenPoint = node.getScreenPoint();
                                    g.drawString("" + ((ObjectWebNode) node).objectName, screenPoint.x, screenPoint.y);
                                }
                            }
                        }
                    }

                    if (this.current != null && this.current.getZ() == Client.getPlane()) {
                        g.setColor(Color.white);
                        g.drawPolygon(this.current.getBounds());
                        this.drawString(g, "Id: " + this.current.getId());
                        this.drawString(g, "Type: " + this.current.type);
                    }

                    int i;
                    WebNode[] var9;
                    int var11;
                    int var13;
                    if (this.testPath != null) {
                        i = 0;
                        var9 = this.testPath;
                        var11 = var9.length;

                        for (var13 = 0; var13 < var11; ++var13) {
                            node = var9[var13];
                            ++i;
                            if (node.isOnMinimap() && node.isReachable()) {
                                g.setColor(Color.green);
                            } else {
                                g.setColor(Color.red);
                            }

                            PaintUtils.drawTile(g, node, "" + i);
                        }
                    }

                    if (this.testPath != null) {
                        i = 0;
                        var9 = this.testPath;
                        var11 = var9.length;

                        for (var13 = 0; var13 < var11; ++var13) {
                            node = var9[var13];
                            if (node.getZ() == Client.getPlane()) {
                                p = node.getPosition().getMinimapPoint();
                                g.setColor(Color.RED);
                                g.fillOval(p.x - 4, p.y - 4, 8, 8);
                                g.setColor(Color.BLUE);
                                g.drawString("" + i, p.x, p.y);
                                ++i;
                            }
                        }

                        if (this.bestTile != null) {
                            g.setColor(Color.YELLOW);
                            p = this.bestTile.getMinimapPoint();
                            screenPoint = this.bestTile.getScreenPoint();
                            g.fillOval(p.x - 4, p.y - 4, 8, 8);
                            g.drawPolygon(this.bestTile.getBounds());
                            g.drawString("Reachable: " + this.bestTile.isReachable(), screenPoint.x, screenPoint.y + 20);
                        }
                    }

                    return;
                }

                node = (WebNode) var2.next();
            } while (node.getZ() != Client.getPlane());

            screenPoint = node.getMinimapPoint();
            Iterator var5 = node.getDestinations().iterator();

            while (var5.hasNext()) {
                node = (WebNode) var5.next();
                p = node.getMinimapPoint();
                if (this.drawArrows) {
                    this.drawArrow(g, 5, screenPoint.x, screenPoint.y, p.x, p.y);
                }

                g.setColor(new Color(255, 255, 255, 100));
                g.drawLine(screenPoint.x, screenPoint.y, p.x, p.y);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        WebNode node;
        Iterator var7;
        if (this.EXPORT.contains(e.getPoint())) {
            WebData.export();
        } else if (this.LOAD.contains(e.getPoint())) {
            WebData.load();
        } else if (this.PATH_TEST.contains(e.getPoint())) {
            WebPathFinder pathFinder = (new WebPathFinder()).includeTiles(new Tile[]{new Tile(3170, 3399)});
            this.testPath = (WebNode[]) pathFinder.findPath(Player.getLocal().getPosition(), this.testTile).toArray(new WebNode[0]);
        } else if (this.REMOVE_DUPLICATED_NODES.contains(e.getPoint())) {
            this.removeDuplicates();
        } else if (this.CONNECT_WITH.contains(e.getPoint())) {
            this.connectingBoth = true;
        } else if (this.REMOVE_NODE.contains(e.getPoint())) {
            WebData.getNodes().remove(this.current);
            this.current = null;
            this.updateNodes();
        } else if (this.REMOVE_DISTANCED_CONNECTIONS.contains(e.getPoint())) {
            WebData.getNodes().remove(this.current);
            this.current = null;
            this.updateNodes();
            var7 = WebData.getNodes().iterator();

            while (var7.hasNext()) {
                node = (WebNode) var7.next();
                ArrayList<WebNode> toRemoveChild = new ArrayList();
                Iterator var5 = node.getDestinations().iterator();

                while (var5.hasNext()) {
                    WebNode child = (WebNode) var5.next();
                    if (node.getZ() == child.getZ() && node.distance(child) > 35) {
                        toRemoveChild.add(child);
                    }
                }

                node.removeDestinations((WebNode[]) toRemoveChild.toArray(new WebNode[toRemoveChild.size()]));
            }

            this.updateNodes();
        } else if (this.ADD_NODE.contains(e.getPoint())) {
            if (this.current != null && this.current.getPosition().getZ() == Player.getLocal().getPosition().getZ()) {
                this.current = new WebNode(Player.getLocal().getPosition(), this.current);
            } else {
                this.current = new WebNode(Player.getLocal().getPosition());
            }

            WebData.getNodes().add(this.current);
        } else {
            String name;
            String action1;
            if (this.ADD_NODE_OBJECT.contains(e.getPoint())) {
                name = JOptionPane.showInputDialog(Main.frame, "Object name", "Object name");
                action1 = JOptionPane.showInputDialog(Main.frame, "Object action", "Object action");
                if (this.current != null) {
                    this.current = new ObjectWebNode(Player.getLocal().getPosition(), this.current, name, action1);
                } else {
                    this.current = new ObjectWebNode(Player.getLocal().getPosition(), name, action1);
                }

                WebData.getNodes().add(this.current);
            } else if (this.ADD_NODE_NPC.contains(e.getPoint())) {
                name = JOptionPane.showInputDialog(Main.frame, "NPC name", "NPC name");
                action1 = JOptionPane.showInputDialog(Main.frame, "NPC action", "NPC action");
                String[] dialogues = JOptionPane.showInputDialog(Main.frame, "NPC Dialogues(Split with \"|\")", "NPC Dialogues").split("\\|");
                if (this.current != null && this.current.getPosition().getZ() == Player.getLocal().getPosition().getZ()) {
                    this.current = new NpcWebNode(Player.getLocal().getPosition(), this.current, name, action1, dialogues);
                } else {
                    this.current = new NpcWebNode(Player.getLocal().getPosition(), name, action1, dialogues);
                }

                WebData.getNodes().add(this.current);
            } else if (this.ADD_NODE_STAIRS.contains(e.getPoint())) {
                name = JOptionPane.showInputDialog(Main.frame, "Stairs name", "Stairs name");
                action1 = JOptionPane.showInputDialog(Main.frame, "Stairs climb up", "Climb-up");
                String action2 = JOptionPane.showInputDialog(Main.frame, "Stairs climb down", "Climb-down");
                if (this.current != null) {
                    this.current = new StairsWebNode(Player.getLocal().getPosition(), this.current, name, action1, action2);
                } else {
                    this.current = new StairsWebNode(Player.getLocal().getPosition(), name, action1, action2);
                }

                WebData.getNodes().add(this.current);
            }
        }

        var7 = WebData.getNodes().iterator();

        while (var7.hasNext()) {
            node = (WebNode) var7.next();
            Point p = node.getPosition().getMinimapPoint();
            if (node.getPosition().getZ() == Client.getPlane() && p.distance(e.getPoint()) <= 15.0D) {
                if (this.connectingBoth) {
                    this.connectingBoth = false;
                    this.current.connectBoth(node);
                } else {
                    this.current = node;
                }
                break;
            }
        }

        e.consume();
    }

    private void removeDuplicates() {
        ArrayList<WebNode> toRemove = new ArrayList();
        Iterator var2 = WebData.getNodes().iterator();

        WebNode node;
        while (var2.hasNext()) {
            node = (WebNode) var2.next();
            Iterator var4 = WebData.getNodes().iterator();

            while (var4.hasNext()) {
                WebNode other = (WebNode) var4.next();
                if (node != other && node.getId() == other.getId()) {
                    toRemove.add(node);
                }
            }
        }

        var2 = toRemove.iterator();

        while (var2.hasNext()) {
            node = (WebNode) var2.next();
            WebData.getNodes().remove(node);
            this.updateNodes();
        }

    }

    private void updateNodes() {
        Iterator var1 = WebData.getNodes().iterator();

        while (var1.hasNext()) {
            WebNode node = (WebNode) var1.next();
            ArrayList<WebNode> toRemove = new ArrayList();
            Iterator var4 = node.getDestinations().iterator();

            while (var4.hasNext()) {
                WebNode child = (WebNode) var4.next();
                if (!WebData.getNodes().contains(child)) {
                    toRemove.add(child);
                }
            }

            node.removeDestinations((WebNode[]) toRemove.toArray(new WebNode[toRemove.size()]));
        }

    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    void drawArrow(Graphics g1, int size, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();
        double dx = (double) (x2 - x1);
        double dy = (double) (y2 - y1);
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance((double) x1, (double) y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);
        g.fillPolygon(new int[]{len, len - size, len - size, len}, new int[]{0, -size, size, 0}, 4);
    }
}
