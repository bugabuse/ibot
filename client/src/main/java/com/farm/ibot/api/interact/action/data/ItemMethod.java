package com.farm.ibot.api.interact.action.data;

public enum ItemMethod {
    ITEM_ON_ITEM("->", 31),
    ITEM_ON_Character("->", 7),
    MAGIC_ON_ITEM("Cast ->", 32),
    CAST("Cast", 25),
    CAST_LUMBRIDGE("Cast-lumbridge", 57),
    BOND_REDEEM("Redeem", 33),
    OPEN("Open", 33),
    EAT("Eat", 33),
    BURY("Bury", 33),
    DRINK("Drink", 33),
    WIELD("Wield", 34),
    EQUIP("Equip", 57, 1, true),
    WEAR("Wear", 34),
    BREAK("Break", 33),
    DROP("Drop", 37),
    DESTROY("Destroy", 37),
    USE("Use", 38),
    REMOVE("Remove", 57),
    RUB("Rub", 36),
    EXAMINE("Examine", 1005),
    LAY("Lay", 33),
    OFFER_GE("Offer", 57, 1, true),
    OFFER_1("Offer-1", 57, 1, true),
    OFFER_5("Offer-5", 57, 2, true),
    OFFER_10("Offer-10", 57, 3, true),
    OFFER_X("Offer-X", 57, 5, true),
    OFFER_ALL("Offer-All", 57, 4, true),
    VALUE("Value", 57, 1, true),
    BUY_1("Buy 1", 57, 2, true),
    BUY_5("Buy 5", 57, 3, true),
    BUY_10("Buy 10", 57, 4, true),
    BUY_50("Buy 50", 57, 5, true),
    WITHDRAW_1("Withdraw-1", 57, 1, true),
    WITHDRAW_5("Withdraw-5", 57, 3, true),
    WITHDRAW_10("Withdraw-10", 57, 4, true),
    WITHDRAW_LAST("Withdraw-Last", 57, 5, true),
    WITHDRAW_X("Withdraw-X", 57, 6, true),
    WITHDRAW_ALL("Withdraw-All", 57, 7, true),
    WITHDRAW_ALL_BUT_ONE("Withdraw-All-but-one", 57, 8, true),
    DEPOSIT_1("Deposit-1", 57, 2, true),
    DEPOSIT_5("Deposit-5", 57, 4, true),
    DEPOSIT_10("Deposit-10", 57, 5, true),
    DEPOSIT_LAST("Deposit-Last", 57, 6, true),
    DEPOSIT_X("Deposit-X", 57, 7, true),
    DEPOSIT_ALL("Deposit-All", 1007, 8, true),
    BOX_DEPOSIT_1("Deposit-1", 57, 2, true),
    BOX_DEPOSIT_5("Deposit-5", 57, 3, true),
    BOX_DEPOSIT_10("Deposit-10", 57, 4, true),
    BOX_DEPOSIT_X("Deposit-X", 57, 6, true),
    BOX_DEPOSIT_ALL("Deposit-All", 57, 5, true);

    public int value;
    public int additionalIndex;
    public String stringValue;
    public boolean isBank;

    private ItemMethod(String stringValue, int value) {
        this(stringValue, value, 0, false);
    }

    private ItemMethod(String stringValue, int value, int index, boolean isBank) {
        this.value = value;
        this.additionalIndex = index;
        this.isBank = isBank;
        this.stringValue = stringValue;
    }

    public static ItemMethod forName(String name) {
        ItemMethod[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            ItemMethod method = var1[var3];
            if (method.stringValue.equalsIgnoreCase(name)) {
                return method;
            }
        }


        return null;
    }
}
