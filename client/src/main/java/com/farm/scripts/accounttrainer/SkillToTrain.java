package com.farm.scripts.accounttrainer;

import com.farm.ibot.api.data.Skill;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.core.script.BotScript;

import java.util.function.BooleanSupplier;

public class SkillToTrain {
    public BotScript script;
    public Skill skill;
    public int maxLevelToTrain;
    private BooleanSupplier stopPredicate;

    public SkillToTrain(BotScript script, BooleanSupplier stopPredicate) {
        this.script = script;
        this.stopPredicate = stopPredicate;
    }

    public SkillToTrain(BotScript script, Skill skill) {
        this.script = script;
        this.skill = skill;
        this.maxLevelToTrain = Random.next(30, 45);
    }

    public SkillToTrain(BotScript script, Skill skill, int maxLevelToTrain) {
        this.script = script;
        this.skill = skill;
        this.maxLevelToTrain = maxLevelToTrain;
    }

    public int getPriority() {
        if (this.stopPredicate != null) {
            return this.stopPredicate.getAsBoolean() ? 0 : 100;
        } else if (this.skill.getRealLevel() < this.maxLevelToTrain) {
            return 100;
        } else {
            return this.skill.equals(AccountTrainer.recentlyTrained) ? 0 : 99 - this.skill.getRealLevel();
        }
    }

    public BooleanSupplier getStopPredicate() {
        return this.stopPredicate;
    }
}
