package com.farm.scripts.zulrahkiller.strategy;

import com.farm.ibot.api.methods.Prayer;
import com.farm.ibot.api.methods.entities.Projectiles;
import com.farm.ibot.api.util.ScriptUtils;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.zulrahkiller.api.Zulrah;

import java.util.Arrays;

public class PrayerStrategy extends Strategy {
    private boolean protectRanged = false;
    private int currentHits = 0;

    public boolean active() {
        return true;
    }

    public boolean isBackground() {
        return true;
    }

    protected void onAction() {
        if (Zulrah.get() == null) {
            this.currentHits = 0;
        } else {
            if (this.currentHits <= 0) {
                this.protectRanged = Zulrah.getCurrentRotationStrategy().isRangedFirst();
                if (Projectiles.getAll((p) -> {
                    return p.getId() == 1046;
                }).length > 0 || Projectiles.getAll((p) -> {
                    return p.getId() == 1044;
                }).length > 0) {
                    ++this.currentHits;
                }
            }

            this.keepOnlyAllowed(Prayer.MYSTIC_MIGHT, Prayer.EAGLE_EYE, Prayer.PROTECT_RANGED, Prayer.PROTECT_MELEE, Prayer.PROTECT_MAGIC);
            if (Zulrah.getCurrentRotationStrategy().isJadMode()) {
                if (this.protectRanged) {
                    this.setEnabled(Prayer.PROTECT_RANGED, true);
                } else {
                    this.setEnabled(Prayer.PROTECT_MAGIC, true);
                }

                if (Projectiles.getAll((p) -> {
                    return p.getId() == 1044;
                }).length > 0) {
                    this.protectRanged = false;
                    this.sleep(200);
                }

                if (Projectiles.getAll((p) -> {
                    return p.getId() == 1046;
                }).length > 0) {
                    this.protectRanged = true;
                    this.sleep(200);
                }
            } else if (!Zulrah.get().isInteractingWithMe() && !Zulrah.isArriving()) {
                if (this.currentHits > 1) {
                    this.setEnabled(Prayer.PROTECT_MAGIC, false);
                    this.setEnabled(Prayer.PROTECT_MELEE, false);
                    this.setEnabled(Prayer.PROTECT_RANGED, false);
                }
            } else {
                switch (Zulrah.getColor()) {
                    case BLUE:
                        this.setEnabled(Prayer.PROTECT_MAGIC, true);
                        break;
                    case GREEN:
                        this.setEnabled(Prayer.PROTECT_RANGED, true);
                        break;
                    case NONE:
                        this.setEnabled(Prayer.PROTECT_MAGIC, false);
                        this.setEnabled(Prayer.PROTECT_MELEE, false);
                        this.setEnabled(Prayer.PROTECT_RANGED, false);
                        break;
                    default:
                        this.setEnabled(Prayer.PROTECT_MAGIC, false);
                        this.setEnabled(Prayer.PROTECT_MELEE, false);
                        this.setEnabled(Prayer.PROTECT_RANGED, false);
                }
            }

            switch (Zulrah.getColor()) {
                case BLUE:
                    this.setEnabled(Prayer.EAGLE_EYE, true);
                    break;
                case GREEN:
                    this.setEnabled(Prayer.MYSTIC_MIGHT, true);
                case NONE:
                default:
                    break;
                case RED:
                    this.setEnabled(Prayer.MYSTIC_MIGHT, true);
            }

        }
    }

    private boolean setEnabled(Prayer prayer, boolean enabled) {
        if (enabled == prayer.isEnabled()) {
            return true;
        } else {
            ScriptUtils.waitForStrategyExecute(this);
            return prayer.setEnabled(enabled);
        }
    }

    private void keepOnlyAllowed(Prayer... allowed) {
        Prayer[] var2 = Prayer.values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            Prayer prayer = var2[var4];
            if (!Arrays.asList(allowed).contains(prayer) && prayer.isEnabled()) {
                prayer.setEnabled(false);
            }
        }

    }
}
