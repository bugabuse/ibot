package com.farm.ibot.api.interfaces;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.world.Screen;
import com.farm.ibot.api.wrapper.Tile;

import java.awt.*;

public interface Animable {
    int getAnimableX();

    int getAnimableY();

    default int getModelHeight() {
        return 0;
    }

    default Point getScreenPoint() {
        return Screen.worldToScreen(this.getAnimableX(), this.getAnimableY(), this.getModelHeight());
    }

    default Tile getPosition() {
        return (new Tile(this.getAnimableX(), this.getAnimableY(), Client.getPlane())).toWorldTile().setModelHeight(this.getModelHeight());
    }

    default Tile getAnimablePosition() {
        return (new Tile(this.getAnimableX(), this.getAnimableY(), Client.getPlane())).setModelHeight(this.getModelHeight());
    }
}
