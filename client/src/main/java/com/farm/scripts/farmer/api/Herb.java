package com.farm.scripts.farmer.api;

public enum Herb {
    RANARR(5295, 207),
    TOADFLAX(5296, 3049);

    public int seedId;
    public int grimyWeedId;

    private Herb(int seedId, int grimyWeedId) {
        this.seedId = seedId;
        this.grimyWeedId = grimyWeedId;
    }
}
