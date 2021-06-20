package com.farm.ibot.api.util.web.osbuddyexchange;

import com.google.gson.annotations.SerializedName;

public class OsbuddyExchangeItem {
    public long fetchTime = System.currentTimeMillis();
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("members")
    public boolean members;
    @SerializedName("buy_average")
    public int buyAverage;
    @SerializedName("sell_average")
    public int sellAverage;
    @SerializedName("overall_average")
    public int overallAverage;

    public int getBuyAverage() {
        return this.buyAverage > 0 ? this.buyAverage : this.overallAverage;
    }

    public int getSellAverage() {
        return this.sellAverage > 0 ? this.sellAverage : this.overallAverage;
    }
}
