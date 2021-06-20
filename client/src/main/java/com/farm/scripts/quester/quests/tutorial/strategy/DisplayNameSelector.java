package com.farm.scripts.quester.quests.tutorial.strategy;

import com.farm.ibot.api.accessors.Widget;
import com.farm.ibot.api.methods.InputBox;
import com.farm.ibot.api.methods.entities.Widgets;
import com.farm.ibot.api.util.Time;
import com.farm.ibot.core.script.Strategy;
import com.farm.ibot.init.AccountData;


public class DisplayNameSelector extends Strategy {
    private boolean emailChecked = false;

    public boolean active() {
        return Widgets.isValid(558);
    }

    public void onAction() {
        Widget nameWidget = Widgets.get(558, 11);
        Widget avabilityWidget = Widgets.get(558, 12);
        if (nameWidget.getText().length() < 1) {
            this.selectName();
        } else if (!avabilityWidget.getText().contains("not available") && !avabilityWidget.getText().contains("unavailable")) {
            if (avabilityWidget.getText().contains("available</col>!")) {
                Widgets.get(558, 18, 9).interact("");
            }
        } else {
            this.selectName();
        }

    }

    private void selectName() {
        Widget nameWidget = Widgets.get(558, 11);
        if (!InputBox.isOpen()) {
            nameWidget.interact("");
            Time.sleep(InputBox::isOpen);
        } else {
            if (!this.emailChecked) {
                InputBox.input(AccountData.current().username.split("@")[0].replace("_", " "));
                this.emailChecked = true;
            } else {
                //InputBox.input(NameGenerator.generate().replace("_", " "));
            }

            Time.sleep(4000);
        }

    }
}