package com.farm.scripts.flaxspinner;

import com.farm.ibot.api.util.string.DynamicString;
import com.farm.ibot.api.util.string.WebConfigDynamicString;
import com.farm.ibot.init.AccountData;
import com.farm.scripts.fisher.util.RequiredItem;

public class Constants {
    public static final DynamicString buyingItem = new WebConfigDynamicString("buying_item", 60000L);
    public static final int ID_FLAX = 1779;
    public static final int ID_LEATHER = 1741;
    public static final int ID_NEEDLE = 1733;
    public static final int ID_UNCUT_OPAL = 1625;
    public static final int ID_CHISEL = 1755;
    public static final int ID_THREAD = 1734;
    public static final RequiredItem[] ITEMS_TRAINING_OPAL = new RequiredItem[]{new RequiredItem(1625, 27, 130, 1), new RequiredItem(1755, 1, 1, 1)};
    public static final RequiredItem[] ITEMS_TRAINING = new RequiredItem[]{new RequiredItem(1741, 26, 100, 1), new RequiredItem(1733, 5, 5, 1), new RequiredItem(1734, 50, 50, 1)};
    public static final RequiredItem[] ITEMS_FLAXING = new RequiredItem[]{new RequiredItem(1779, 28, 4000, 1)};

    public static RequiredItem[] getTrainingItems() {
        if (buyingItem.intValue() == 1) {
            return ITEMS_TRAINING_OPAL;
        } else if (buyingItem.intValue() == 2) {
            return ITEMS_TRAINING;
        } else {
            return AccountData.current().getId() % 2 == 0 ? ITEMS_TRAINING_OPAL : ITEMS_TRAINING;
        }
    }
}
