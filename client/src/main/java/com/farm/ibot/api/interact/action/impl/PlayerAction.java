package com.farm.ibot.api.interact.action.impl;

import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.interact.action.data.PlayerMethod;
import com.farm.ibot.api.util.Random;

import java.awt.*;

public class PlayerAction extends Action {
    public PlayerAction(int index, int playerId, int x, int y) {
        super(0, 0, index, playerId, "", "", 0, 0);
        this.clickPointX = x;
        this.clickPointY = y;
    }

    public static Action create(PlayerMethod method, Player player) {
        if (player != null) {
            Point p = player.getPosition().getScreenPoint();
            p.translate(Random.next(-30, 30), Random.next(-30, 30));
            return new PlayerAction(method.value, player.getIndex(), p.x, p.y);
        } else {
            return new Action("PlayerAction");
        }
    }
}
