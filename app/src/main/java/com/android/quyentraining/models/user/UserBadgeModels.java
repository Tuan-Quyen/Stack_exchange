package com.android.quyentraining.models.user;

import com.google.gson.annotations.SerializedName;

public class UserBadgeModels {
    @SerializedName("bronze")
    public Integer bronze;
    @SerializedName("gold")
    public Integer gold;
    @SerializedName("silver")
    public Integer silver;

    public Integer getBronze() {
        return bronze;
    }

    public Integer getGold() {
        return gold;
    }

    public Integer getSilver() {
        return silver;
    }
}
