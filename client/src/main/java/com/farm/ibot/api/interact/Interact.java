package com.farm.ibot.api.interact;

import com.farm.ibot.api.interact.impl.HybridInteractHandler;

public class Interact {
    public static InteractHandler interactHandler = new HybridInteractHandler();
    public static boolean useNaturalMouse = false;
}
