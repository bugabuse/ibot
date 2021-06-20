package com.farm.scripts.quester.quests.tutorialnew.strategy;

import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.methods.input.Mouse;
import com.farm.ibot.api.util.Random;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;
import com.farm.scripts.quester.quests.tutorialnew.TutorialStateNew;

public class CharacterInterface extends Strategy {
    public boolean active() {
        return Widgets.isValid(269);
    }

    public void onAction() {
        int i;
        short x;
        int y;
        int click;
        for (i = 0; i < 7; ++i) {
            x = 159;
            y = 76 + i * 27;

            for (click = 0; click < Random.next(1, 9); ++click) {
                Mouse.clickBox(x, y, x + 40, y + 35);
                Time.sleep(50, 100);
            }
        }

        for (i = 0; i < 5; ++i) {
            x = 461;
            y = 76 + i * 27;

            for (click = 0; click < Random.next(1, 6); ++click) {
                Mouse.clickBox(x, y, x + 40, y + 35);
                Time.sleep(50, 100);
            }

            Time.sleep(200, 850);
        }

        if (Random.next(0, 2) == 1) {
            Mouse.clickBox(451, 282, 500, 301);
        }

        Time.sleep(1000, 3000);
        Mouse.clickBox(245, 271, 302, 297);
        Widgets.forId(17629283).interact("");
        TutorialStateNew.waitStateChange();
    }
}
