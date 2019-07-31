package com.android.quyentraining.models.tag;

import com.google.gson.annotations.SerializedName;

public class TagsModels {
    @SerializedName("name")
    public String nameTag;
    @SerializedName("count")
    public Integer numberTag;
    public TagsModels() {
    }

    public String getNameTag() {
        return nameTag;
    }

    public Integer getNumberTag() {
        return numberTag;
    }
}
