package com.farm.ibot.api.wrapper;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.util.ArrayList;
import java.util.List;

public class SimplePolygon implements Shape {
    private static final int GROW = 16;
    protected int[] x;
    protected int[] y;
    protected int left;
    protected int right;

    public SimplePolygon() {
        this(new int[32], new int[32], 16, 15);
    }

    public SimplePolygon(int[] x, int[] y, int length) {
        this(x, y, 0, length - 1);
    }

    public SimplePolygon(int[] x, int[] y, int left, int right) {
        this.x = x;
        this.y = y;
        this.left = left;
        this.right = right;
    }

    public void pushLeft(int xCoord, int yCoord) {
        --this.left;
        if (this.left < 0) {
            this.expandLeft(16);
        }

        this.x[this.left] = xCoord;
        this.y[this.left] = yCoord;
    }

    public void popLeft() {
        ++this.left;
    }

    protected void expandLeft(int grow) {
        int[] nx = new int[this.x.length + grow];
        System.arraycopy(this.x, 0, nx, grow, this.x.length);
        this.x = nx;
        int[] ny = new int[nx.length];
        System.arraycopy(this.y, 0, ny, grow, this.y.length);
        this.y = ny;
        this.left += grow;
        this.right += grow;
    }

    public void pushRight(int xCoord, int yCoord) {
        ++this.right;
        if (this.right >= this.x.length) {
            this.expandRight(16);
        }

        this.x[this.right] = xCoord;
        this.y[this.right] = yCoord;
    }

    public void popRight() {
        --this.right;
    }

    protected void expandRight(int grow) {
        int[] nx = new int[this.x.length + grow];
        System.arraycopy(this.x, 0, nx, 0, this.x.length);
        this.x = nx;
        int[] ny = new int[nx.length];
        System.arraycopy(this.y, 0, ny, 0, this.y.length);
        this.y = ny;
    }

    public int getX(int index) {
        return this.x[this.left + index];
    }

    public int getY(int index) {
        return this.y[this.left + index];
    }

    public int size() {
        return this.right - this.left + 1;
    }

    public List<Point> toRuneLitePointList() {
        List<Point> out = new ArrayList(this.size());

        for (int i = this.left; i <= this.right; ++i) {
            out.add(new Point(this.x[i], this.y[i]));
        }

        return out;
    }

    public void copyTo(int[] xDest, int[] yDest, int offset) {
        System.arraycopy(this.x, this.left, xDest, offset, this.size());
        System.arraycopy(this.y, this.left, yDest, offset, this.size());
    }

    public void appendTo(SimplePolygon other) {
        int size = this.size();
        if (size > 0) {
            other.expandRight(size);
            this.copyTo(other.x, other.y, other.right + 1);
            other.right += size;
        }
    }

    public void reverse() {
        int half = this.size() / 2;

        for (int i = 0; i < half; ++i) {
            int li = this.left + i;
            int ri = this.right - i;
            int tx = this.x[li];
            int ty = this.y[li];
            this.x[li] = this.x[ri];
            this.y[li] = this.y[ri];
            this.x[ri] = tx;
            this.y[ri] = ty;
        }

    }

    public void intersectWithConvex(SimplePolygon convex) {
        int[] tx = new int[this.size()];
        int[] ty = new int[tx.length];
        int cx1 = convex.x[convex.right];
        int cy1 = convex.y[convex.right];

        for (int ci = convex.left; ci <= convex.right; ++ci) {
            if (this.size() < 3) {
                return;
            }

            int tRight = this.right;
            int tLeft = this.left;
            int[] tmpX = this.x;
            int[] tmpY = this.y;
            this.x = tx;
            this.y = ty;
            this.left = 0;
            this.right = -1;
            tx = tmpX;
            ty = tmpY;
            int cx2 = convex.x[ci];
            int cy2 = convex.y[ci];
            int tx1 = tmpX[tRight];
            int ty1 = tmpY[tRight];

            for (int ti = tLeft; ti <= tRight; ++ti) {
                int tx2 = tx[ti];
                int ty2 = ty[ti];
                int p1 = (cx2 - cx1) * (ty1 - cy1) - (cy2 - cy1) * (tx1 - cx1);
                int p2 = (cx2 - cx1) * (ty2 - cy1) - (cy2 - cy1) * (tx2 - cx1);
                if (p1 < 0 && p2 < 0) {
                    this.pushRight(tx2, ty2);
                } else if (p1 >= 0 != p2 >= 0) {
                    long nota = (long) (cx1 * cy2 - cy1 * cx2);
                    long clue = (long) (tx1 * ty2 - ty1 * tx2);
                    long div = (long) ((cx1 - cx2) * (ty1 - ty2) - (cy1 - cy2) * (tx1 - tx2));
                    this.pushRight((int) ((nota * (long) (tx1 - tx2) - (long) (cx1 - cx2) * clue) / div), (int) ((nota * (long) (ty1 - ty2) - (long) (cy1 - cy2) * clue) / div));
                    if (p1 >= 0) {
                        this.pushRight(tx2, ty2);
                    }
                }

                tx1 = tx2;
                ty1 = ty2;
            }

            cx1 = cx2;
            cy1 = cy2;
        }

    }

    public Rectangle getBounds() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int i = this.left; i <= this.right; ++i) {
            int xs = this.x[i];
            int ys = this.y[i];
            if (xs < minX) {
                minX = xs;
            }

            if (xs > maxX) {
                maxX = xs;
            }

            if (ys < minY) {
                minY = ys;
            }

            if (ys > maxY) {
                maxY = ys;
            }
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    public Rectangle2D getBounds2D() {
        Rectangle b = this.getBounds();
        return new Float((float) b.x, (float) b.y, (float) b.width, (float) b.height);
    }

    public boolean contains(double cx, double cy) {
        if (this.size() < 3) {
            return false;
        } else {
            return (this.crossings(cx, cy, false) & 1) != 0;
        }
    }

    private int crossings(double cx, double cy, boolean swap) {
        int collisions = 0;
        int[] x = this.x;
        int[] y = this.y;
        if (swap) {
            y = this.x;
            x = this.y;
        }

        int x0 = x[this.right];
        int y0 = y[this.right];

        int y1;
        for (int i = this.left; i <= this.right; y0 = y1) {
            int x1 = x[i];
            y1 = y[i];
            if (y0 != y1) {
                double dy0 = (double) y0;
                double dy1 = (double) y1;
                if (cy <= dy0 != cy <= dy1) {
                    double dx0 = (double) x0;
                    double dx1 = (double) x1;
                    boolean left = cx < dx0;
                    if (left == cx < dx1) {
                        if (!left) {
                            ++collisions;
                        }
                    } else if ((dx1 - dx0) * (cy - dy0) - (cx - dx0) * (dy1 - dy0) > 0.0D == dy0 > dy1) {
                        ++collisions;
                    }
                }
            }

            ++i;
            x0 = x1;
        }

        return collisions;
    }

    public boolean contains(Point2D p) {
        return this.contains(p.getX(), p.getY());
    }

    public boolean intersects(double x0, double y0, double w, double h) {
        double x1 = x0 + w;
        double y1 = y0 + h;
        return this.crossings(x0, y0, false) != this.crossings(x1, y0, false) || this.crossings(x0, y1, false) != this.crossings(x1, y1, false) || this.crossings(x0, y0, true) != this.crossings(x0, y1, true) || this.crossings(x1, y0, true) != this.crossings(x1, y1, true);
    }

    public boolean intersects(Rectangle2D r) {
        return this.intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public boolean contains(double x, double y, double w, double h) {
        if (!this.getBounds().contains(x, y, w, h)) {
            return false;
        } else {
            return !this.intersects(x, y, w, h);
        }
    }

    public boolean contains(Rectangle2D r) {
        return this.contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return (PathIterator) (at == null ? new SimplePolygon.SimpleIterator() : new SimplePolygon.TransformIterator(at));
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return this.getPathIterator(at);
    }

    private class TransformIterator extends SimplePolygon.SimpleIterator {
        private final AffineTransform transform;

        TransformIterator(AffineTransform transform) {
            super(null);
            this.transform = transform;
        }

        public int currentSegment(float[] coords) {
            int v = super.currentSegment(coords);
            this.transform.transform(coords, 0, coords, 0, 2);
            return v;
        }

        public int currentSegment(double[] coords) {
            int v = super.currentSegment(coords);
            this.transform.transform(coords, 0, coords, 0, 2);
            return v;
        }
    }

    private class SimpleIterator implements PathIterator {
        private int i;

        private SimpleIterator() {
            this.i = -1;
        }

        // $FF: synthetic method
        SimpleIterator(Object x1) {
            this();
        }

        public int getWindingRule() {
            return 0;
        }

        public boolean isDone() {
            return SimplePolygon.this.size() == 0 || this.i > SimplePolygon.this.right;
        }

        public void next() {
            if (this.i == -1) {
                this.i = SimplePolygon.this.left;
            } else {
                ++this.i;
            }

        }

        public int currentSegment(float[] coords) {
            if (this.i == -1) {
                coords[0] = (float) SimplePolygon.this.x[SimplePolygon.this.right];
                coords[1] = (float) SimplePolygon.this.y[SimplePolygon.this.right];
                return 0;
            } else {
                coords[0] = (float) SimplePolygon.this.x[this.i];
                coords[1] = (float) SimplePolygon.this.y[this.i];
                return 1;
            }
        }

        public int currentSegment(double[] coords) {
            if (this.i == -1) {
                coords[0] = (double) SimplePolygon.this.x[SimplePolygon.this.right];
                coords[1] = (double) SimplePolygon.this.y[SimplePolygon.this.right];
                return 0;
            } else {
                coords[0] = (double) SimplePolygon.this.x[this.i];
                coords[1] = (double) SimplePolygon.this.y[this.i];
                return 1;
            }
        }
    }
}
