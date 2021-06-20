package com.farm.scripts.fisher;

import com.farm.ibot.api.wrapper.Tile;
import com.farm.scripts.fisher.util.RequiredItem;

public class FishingConfig {
    private String fishingMethod;
    private RequiredItem[] fishingEquipment;
    private Tile spotLocation;
    private int fishingDistance;
    private boolean usingDepositBox;
    private boolean bankingEnabled;

    public FishingConfig(String fishingMethod, RequiredItem[] fishingEquipment, Tile spotLocation, int fishingDistance, boolean bankingEnabled, boolean usingDepositBox) {
        this.fishingMethod = fishingMethod;
        this.fishingEquipment = fishingEquipment;
        this.spotLocation = spotLocation;
        this.fishingDistance = fishingDistance;
        this.bankingEnabled = bankingEnabled;
        this.usingDepositBox = usingDepositBox;
    }

    public FishingConfig(String fishingMethod, RequiredItem[] fishingEquipment, Tile spotLocation, int fishingDistance, boolean bankingEnabled) {
        this.fishingMethod = fishingMethod;
        this.fishingEquipment = fishingEquipment;
        this.spotLocation = spotLocation;
        this.fishingDistance = fishingDistance;
        this.bankingEnabled = bankingEnabled;
    }

    public String getFishingMethod() {
        return this.fishingMethod;
    }

    public RequiredItem[] getFishingEquipment() {
        return this.fishingEquipment;
    }

    public Tile getSpotLocation() {
        return this.spotLocation;
    }

    public int getFishingDistance() {
        return this.fishingDistance;
    }

    public boolean isBankingEnabled() {
        return this.bankingEnabled;
    }

    public boolean isUsingDepositBox() {
        return this.usingDepositBox;
    }
}
