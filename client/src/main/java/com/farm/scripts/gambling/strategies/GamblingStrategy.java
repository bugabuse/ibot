package com.farm.scripts.gambling.strategies;

import com.farm.ibot.api.util.PaintTimer;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.core.script.Strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GamblingStrategy extends Strategy {
    public static HashMap<String, Integer> playerBets = new HashMap();
    public static PaintTimer timeUntilDraw = new PaintTimer();
    public static String winner = null;
    private HashSet<String> shoutQueue = new HashSet();

    public static boolean isBettingClosed() {
        return timeUntilDraw.getElapsedSeconds() >= 0L || winner != null;
    }

    public static void openNewBetting() {
        playerBets = new HashMap();
        timeUntilDraw = new PaintTimer(System.currentTimeMillis() + 30000000L);
        winner = null;
    }

    public static void startTimer() {
        if (!isGameRunning()) {
            timeUntilDraw = new PaintTimer(System.currentTimeMillis() + 10000L);
            winner = null;
        }

    }

    public static boolean isGameRunning() {
        return playerBets.size() > 0;
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        if (isBettingClosed()) {
            if (winner == null) {
                if (playerBets.size() > 0) {
                    this.makeWinner();
                }
            } else {

            }
        } else {
            this.handleShouting();
        }

    }

    private void handleShouting() {
        this.shoutQueue.forEach((s) -> {
            System.out.println("Shouting: " + s);
        });
        this.shoutQueue.clear();
    }

    private void makeWinner() {
        winner = (String) (new ArrayList(playerBets.keySet())).get(Random.next(0, playerBets.size()));
        String amountWin = "250k";
        String chances = "48%";
        this.shout(String.format("%s has won %s with a chance of %s", winner, amountWin, chances));

    }

    private void shout(String message) {
        this.shoutQueue.add(message);
    }
}
