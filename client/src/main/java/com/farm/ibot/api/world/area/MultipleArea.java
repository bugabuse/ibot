package com.farm.ibot.api.world.area;

import com.farm.ibot.api.wrapper.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MultipleArea extends Area {
    private ArrayList<Area> areas = new ArrayList();
    private ArrayList<Area> excludedAreas = new ArrayList();

    public MultipleArea() {
    }

    public MultipleArea(Area... areas) {
        this.areas = new ArrayList(Arrays.asList(areas));
    }

    public MultipleArea(Area[] areas, Area[] excludedAreas) {
        this.areas = new ArrayList(Arrays.asList(areas));
        this.excludedAreas = new ArrayList(Arrays.asList(excludedAreas));
    }

    public MultipleArea exclude(Area... areas) {
        Collections.addAll(this.excludedAreas, areas);
        return this;
    }

    public MultipleArea add(Area... areas) {
        Collections.addAll(this.areas, areas);
        return this;
    }

    public boolean contains(Tile tile) {
        return !this.excludedAreas.stream().anyMatch((a) -> {
            return a.contains(tile);
        }) && this.areas.stream().anyMatch((a) -> {
            return a.contains(tile);
        });
    }

    public Tile centerTile() {
        return ((Area) this.areas.get(0)).centerTile();
    }
}
