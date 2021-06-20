package com.farm.scripts.thiever.strategies;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.accessors.Npc;
import com.farm.ibot.api.accessors.Player;
import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.interact.action.Action;
import com.farm.ibot.api.methods.entities.Npcs;
import com.farm.ibot.api.methods.walking.Walking;
import com.farm.ibot.api.methods.walking.WebWalking;
import com.farm.ibot.api.util.Sorting;
import com.farm.ibot.api.util.StringUtils;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.api.wrapper.Tile;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.Settings;
import com.google.common.primitives.Ints;

public class ThieveManStrategy extends Strategy {
    private static ThieveManStrategy.ThievingNpc THIEVING_MAN = new ThieveManStrategy.ThievingNpc(1, 1, new Tile(3237, 3217), new String[]{"Man", "Woman"});
    private static ThieveManStrategy.ThievingNpc THIEVING_FARMER = new ThieveManStrategy.ThievingNpc(1, 10, new Tile(3226, 3312), new String[]{"Farmer"});
    private static ThieveManStrategy.ThievingNpc WARRIOR_WOMAN = new ThieveManStrategy.ThievingNpc(1, 34, new Tile(3206, 3484), new String[]{"Warrior woman"});
    private static ThieveManStrategy.ThievingNpc MASTER_FARMER = new ThieveManStrategy.ThievingNpc(3, 38, new Tile(3083, 3248), new String[]{"Master Farmer"});
    private static ThieveManStrategy.ThievingNpc[] NPCS;

    static {
        NPCS = new ThieveManStrategy.ThievingNpc[]{THIEVING_MAN, THIEVING_FARMER, MASTER_FARMER};
    }

    Npc man = null;

    public static ThieveManStrategy.ThievingNpc getThievingNpc() {
        ThieveManStrategy.ThievingNpc best = null;
        ThieveManStrategy.ThievingNpc[] var1 = NPCS;
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ThieveManStrategy.ThievingNpc npc = var1[var3];
            if (Skill.THIEVING.getRealLevel() >= npc.level) {
                best = npc;
            }
        }

        return best;
    }

    public boolean active() {
        return true;
    }

    protected void onAction() {
        Settings.useLongSleep = false;

        if (Client.getRunEnergy() > 20) {
            Walking.setRun(true);
        }


        if (Skill.HITPOINTS.getCurrentLevel() > getThievingNpc().hpToHeal) {

            if (Player.getLocal().getModelHeight() != 1000) {

                if (WebWalking.walkTo(getThievingNpc().tile, 20, new Tile[0])) {

                    if (this.man != null && this.man.exists()) {

                        if (!this.man.isReachable()) {
                            try {
                                WebWalking.walkToEnsurePath(this.man.getPosition(), 0, new Tile[0]);
                            } catch (Exception var2) {

                            }

                        } else {

                            Action.isSpamClicking = true;
                            this.man.interact("Pickpocket");
                            Action.isSpamClicking = false;
                            Time.sleep(100, 400);
                        }
                    } else {
                        this.man = (Npc) Sorting.getNearest(getThievingNpc().tile, Npcs.getAll((n) -> {
                            return n.getPosition().distance(getThievingNpc().tile) < 30 && (StringUtils.containsEqual(n.getName(), getThievingNpc().names) || Ints.contains(getThievingNpc().ids, n.getId()));
                        }));
                        if (this.man == null) {
                            WebWalking.walkTo(getThievingNpc().tile, 2, new Tile[0]);
                        }

                    }
                }
            }
        }
    }

    public static class ThievingNpc {
        public int level;
        public int[] ids = new int[0];
        public Tile tile;
        public String[] names;
        public int hpToHeal;

        public ThievingNpc(int hpToHeal, int level, Tile tile, String... names) {
            this.hpToHeal = hpToHeal;
            this.level = level;
            this.tile = tile;
            this.names = names;
        }

        public ThievingNpc(int hpToHeal, int level, Tile tile, int... ids) {
            this.hpToHeal = hpToHeal;
            this.level = level;
            this.tile = tile;
            this.ids = ids;
        }
    }
}
