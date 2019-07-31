package com.android.quyentraining.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UsersModels implements Serializable {
    @SerializedName("display_name")
    public String userName;
    @SerializedName("profile_image")
    public String userImage;
    @SerializedName("reputation")
    public Integer reputation;
    @SerializedName("badge_counts")
    public UserBadgeModels userBadgeModels;

    public UserBadgeModels getUserBadgeModels() {
        return userBadgeModels;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getReputation() {
        return reputation;
    }

    public String getUserImage() {
        return userImage;
    }
}
