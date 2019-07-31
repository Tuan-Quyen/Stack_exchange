package com.android.quyentraining.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OwnerQuestionModels implements Serializable {
    @SerializedName("reputation")
    private Integer reputation;
    @SerializedName("display_name")
    private String name;
    @SerializedName("user_id")
    private Integer userID;
    @SerializedName("profile_image")
    private String image;

    public String getImage() {
        return image;
    }

    public Integer getReputation() {
        return reputation;
    }

    public String getName() {
        return name;
    }

    public Integer getUserID() {
        return userID;
    }
}
