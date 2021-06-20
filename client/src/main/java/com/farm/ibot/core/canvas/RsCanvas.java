package com.farm.ibot.core.canvas;

import com.farm.ibot.api.interfaces.PaintHandler;
import com.farm.ibot.core.Bot;

import javax.accessibility.AccessibleContext;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class RsCanvas extends Canvas {
    private static final int APPLET_WIDTH = 765;
    private static final int APPLET_HEIGHT = 503;
    private final Bot bot;
    public Canvas canvas;
    public BufferedImage debugImage = new BufferedImage(765, 503, 1);
    public BufferedImage gameImage = new BufferedImage(765, 503, 1);
    public PaintHandler paintHandler;
    Graphics originalGraphics;

    public RsCanvas(Bot bot, Canvas canvas) {
        super(canvas.getGraphicsConfiguration());
        this.canvas = canvas;
        this.bot = bot;
        this.originalGraphics = this.getGraphics();
        canvas.setPreferredSize(new Dimension(765, 503));
    }

    public void addMouseListener(MouseListener listener) {
        this.canvas.addMouseListener(listener);
    }

    public void removeMouseListener(MouseListener listener) {
        this.canvas.removeMouseListener(listener);
    }

    public int hashCode() {
        return this.canvas.hashCode();
    }

    public Graphics getGraphics() {
        Graphics graphics = this.debugImage.getGraphics();
        graphics.drawImage(this.gameImage, 0, 0, (ImageObserver) null);

        try {
            if (this.paintHandler != null) {
                this.paintHandler.onPaint(graphics);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        graphics.dispose();
        super.getGraphics().drawImage(this.debugImage, 0, 0, (ImageObserver) null);
        return this.gameImage.getGraphics();
    }

    public void setLocation(int x, int y) {
        this.canvas.setLocation(0, 0);
        this.canvas.setPreferredSize(new Dimension(765, 503));
        this.setPreferredSize(new Dimension(765, 503));
    }

    public BufferedImage getGameImage() {
        return this.gameImage;
    }

    public void update(Graphics g) {
        this.canvas.update(g);
    }

    public void createBufferStrategy(int numBuffers) {
        super.createBufferStrategy(numBuffers);
        this.canvas.createBufferStrategy(numBuffers);
    }

    public void createBufferStrategy(int numBuffers, BufferCapabilities caps) throws AWTException {
        super.createBufferStrategy(numBuffers, caps);
        this.canvas.createBufferStrategy(numBuffers, caps);
    }

    public BufferStrategy getBufferStrategy() {
        return this.canvas.getBufferStrategy();
    }

    public AccessibleContext getAccessibleContext() {
        return this.canvas.getAccessibleContext();
    }
}
