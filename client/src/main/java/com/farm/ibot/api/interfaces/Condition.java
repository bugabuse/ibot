package com.farm.ibot.api.interfaces;

public interface Condition {
    default void beforeCheck() {
    }

    boolean active();
}
