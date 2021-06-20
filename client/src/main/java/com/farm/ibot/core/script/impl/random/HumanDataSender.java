package com.farm.ibot.core.script.impl.random;

import com.farm.ibot.api.util.Random;
import com.farm.ibot.core.Bot;
import com.farm.ibot.core.script.RandomEvent;
import com.farm.ibot.init.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;

public class HumanDataSender extends RandomEvent {
    private ArrayList<HumanDataSender.ClickData> rootData = new ArrayList();
    private ArrayList<HumanDataSender.ClickData> currentData = new ArrayList();
    private int currentClickIndex = 0;
    private long nextLoop = -1L;

    public void onStart() {

        this.load();
    }

    public int onLoop() {
        if (System.currentTimeMillis() < this.nextLoop) {
            return (int) (this.nextLoop - System.currentTimeMillis());
        } else {
            if (this.rootData.size() == 0) {
                this.onStart();
            }

            if (this.currentData == null) {
                this.currentData = this.randomize(this.rootData);
                this.currentClickIndex = 0;
                return 1;
            } else {
                ++this.currentClickIndex;
                if (this.currentData.size() > this.currentClickIndex) {
                    HumanDataSender.ClickData click = (HumanDataSender.ClickData) this.currentData.get(this.currentClickIndex);

                    if (click.timeDiff > 0) {
                        this.nextLoop = System.currentTimeMillis() + (long) click.timeDiff;
                    }
                } else {
                    this.currentData = null;
                }

                return 1;
            }
        }
    }

    public long checkInterval() {
        return 100L;
    }

    public boolean isEnabled() {
        return Settings.useHumanData && Bot.get().getScriptHandler().getCurrentlyExecuting().getName().contains("Firemaker");
    }

    public boolean isBackground() {
        return true;
    }

    private ArrayList<HumanDataSender.ClickData> randomize(ArrayList<HumanDataSender.ClickData> data) {
        ArrayList<HumanDataSender.ClickData> temp = new ArrayList();
        Iterator var3 = data.iterator();

        while (var3.hasNext()) {
            HumanDataSender.ClickData click = (HumanDataSender.ClickData) var3.next();
            temp.add(new HumanDataSender.ClickData(click.timeDiff, click.button, click.x + Random.next(-2, 2), click.y + Random.next(-2, 2)));
        }

        return temp;
    }

    private void load() {
        try {
            Iterator var1 = Files.readAllLines(Settings.getPath("/humandata/firemaking.txt")).iterator();

            while (var1.hasNext()) {
                String line = (String) var1.next();
                String[] s = line.split(",");
                if (s.length > 2) {
                    this.rootData.add(new HumanDataSender.ClickData(Integer.parseInt(s[0]), Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[4])));
                }
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    class ClickData {
        public int timeDiff;
        public int button;
        public int x;
        public int y;

        public ClickData(int timeDiff, int button, int x, int y) {
            this.timeDiff = timeDiff;
            this.button = button;
            this.x = x;
            this.y = y;
        }
    }
}
