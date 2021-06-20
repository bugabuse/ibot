package com.farm.ibot.api.util;

import com.farm.ibot.api.accessors.Client;
import com.farm.ibot.api.data.Skill;

public class SkillTracker {
    public Skill skill;
    public int startExperience = -1;
    public PaintTimer timer = new PaintTimer();

    public SkillTracker(Skill skill) {
        this.skill = skill;
    }

    public int getHourRatio() {
        return this.timer.getHourRatio(this.getExpGained());
    }

    public int getExpGained() {
        if (Client.isInGame() && this.startExperience < 10) {
            this.reset();
        }

        return this.skill.getExperience() - this.startExperience;
    }

    public void reset() {
        this.timer.reset();
        this.startExperience = this.skill.getExperience();
    }

    public String getPaintString() {
        return StringUtils.firstLetterCapitalized(this.skill.name()) + ":" + this.getExpGained() + "(" + this.getHourRatio() / 1000 + "k)";
    }
}
