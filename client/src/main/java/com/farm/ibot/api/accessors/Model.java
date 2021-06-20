package com.farm.ibot.api.accessors;

import com.farm.ibot.api.accessors.interfaces.IModel;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.HookName;
import com.farm.ibot.api.wrapper.Jarvis;
import com.farm.ibot.api.wrapper.Vertex;
import com.farm.ibot.core.Bot;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Model extends Renderable {
    public Model(Object instance) {
        super(instance);
    }

    public static IModel getModelInterface() {
        return Bot.get().accessorInterface.modelInterface;
    }

    @HookName("Model.IndicesLength")
    public int getIndicesLength() {
        return getModelInterface().getIndicesLength(this.instance);
    }

    @HookName("Model.IndicesX")
    public int[] getIndicesX() {
        return getModelInterface().getIndicesX(this.instance);
    }

    @HookName("Model.IndicesY")
    public int[] getIndicesY() {
        return getModelInterface().getIndicesY(this.instance);
    }

    @HookName("Model.IndicesZ")
    public int[] getIndicesZ() {
        return getModelInterface().getIndicesZ(this.instance);
    }

    @HookName("Model.VerticesLength")
    public int getVerticesLength() {
        return getModelInterface().getVerticesLength(this.instance);
    }

    @HookName("Model.VerticesX")
    public int[] getVerticesX() {
        return getModelInterface().getVerticesX(this.instance);
    }

    @HookName("Model.VerticesY")
    public int[] getVerticesY() {
        return getModelInterface().getVerticesY(this.instance);
    }

    @HookName("Model.VerticesZ")
    public int[] getVerticesZ() {
        return getModelInterface().getVerticesZ(this.instance);
    }

    public Polygon[] getTriangles(int orientation, int gridX, int gridY, int z) {
        ArrayList<Polygon> polygons = new ArrayList();
        int[] indices1 = this.getIndicesX();
        int[] indices2 = this.getIndicesY();
        int[] indices3 = this.getIndicesZ();
        int[] verticesX = this.getVerticesX();
        int[] verticesY = this.getVerticesY();
        int[] verticesZ = this.getVerticesZ();
        int len = indices1.length;
        orientation &= 16383;
        int sin = Screen.SINE[orientation];
        int cos = Screen.COSINE[orientation];

        int i;
        for (i = 0; i < verticesX.length; ++i) {
            int vertX = verticesX[i] * cos + verticesZ[i] * sin >> 15 >> 1;
            int vertZ = verticesZ[i] * cos - verticesX[i] * sin >> 15 >> 1;
            verticesX[i] = vertX;
            verticesZ[i] = vertZ;
        }

        for (i = 0; i < len; ++i) {
            Point p1 = Screen.worldToScreenNew(gridX + verticesX[indices1[i]], gridY + verticesZ[indices1[i]], -verticesY[indices1[i]] + z);
            Point p2 = Screen.worldToScreenNew(gridX + verticesX[indices2[i]], gridY + verticesZ[indices2[i]], -verticesY[indices2[i]] + z);
            Point p3 = Screen.worldToScreenNew(gridX + verticesX[indices3[i]], gridY + verticesZ[indices3[i]], -verticesY[indices3[i]] + z);
            if (p1.x >= 0 && p2.x >= 0 && p3.x >= 0) {
                polygons.add(new Polygon(new int[]{p1.x, p2.x, p3.x}, new int[]{p1.y, p2.y, p3.y}, 3));
            }
        }

        return (Polygon[]) polygons.toArray(new Polygon[polygons.size()]);
    }

    public List<Vertex> getVertices() {
        int[] verticesX = this.getVerticesX();
        int[] verticesY = this.getVerticesY();
        int[] verticesZ = this.getVerticesZ();
        List<Vertex> vertices = new ArrayList(verticesX.length);

        for (int i = 0; i < verticesX.length; ++i) {
            Vertex v = new Vertex(verticesX[i], verticesY[i], verticesZ[i]);
            vertices.add(v);
        }

        return vertices;
    }

    public ArrayList<Point> getConvexHullPoints(int localX, int localY, int orientation, int tileHeight) {
        List<Vertex> vertices = this.getVertices();

        for (int i = 0; i < vertices.size(); ++i) {
            Vertex v = (Vertex) vertices.get(i);
            vertices.set(i, v.rotate(orientation));
        }

        ArrayList<Point> points = new ArrayList();
        Iterator var11 = vertices.iterator();

        while (var11.hasNext()) {
            Vertex v = (Vertex) var11.next();
            Point p = Screen.worldToScreenNew(localX - v.getX(), localY - v.getZ(), tileHeight + v.getY());
            if (p != null) {
                points.add(p);
            }
        }

        return points;
    }

    public Polygon getConvexHull(int localX, int localY, int orientation, int tileHeight) {
        List<Vertex> vertices = this.getVertices();

        for (int i = 0; i < vertices.size(); ++i) {
            Vertex v = (Vertex) vertices.get(i);
            vertices.set(i, v.rotate(orientation));
        }

        List<Point> points = new ArrayList();
        Iterator var12 = vertices.iterator();

        Point point;
        while (var12.hasNext()) {
            Vertex v = (Vertex) var12.next();
            point = Screen.worldToScreenNew(localX - v.getX(), localY - v.getZ(), tileHeight + v.getY());
            if (point != null) {
                points.add(point);
            }
        }

        points = Jarvis.convexHull(points);
        if (points == null) {
            return null;
        } else {
            Polygon p = new Polygon();
            Iterator var14 = points.iterator();

            while (var14.hasNext()) {
                point = (Point) var14.next();
                p.addPoint((int) point.getX(), (int) point.getY());
            }

            return p;
        }
    }
}
