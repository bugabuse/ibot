package com.farm.ibot.core.script;

import java.util.ArrayList;

public class StrategyContainer extends ArrayList<Strategy> {
    private final String name;

    public StrategyContainer() {
        this("unknown");
    }

    public StrategyContainer(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
