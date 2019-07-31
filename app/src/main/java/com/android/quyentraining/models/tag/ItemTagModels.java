package com.android.quyentraining.models.tag;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemTagModels implements Serializable {
    @SerializedName("items")
    private ArrayList<TagsModels> items = null;

    public ArrayList<TagsModels> getItems() {
        return items != null ? items : new ArrayList<TagsModels>();
    }
    @SerializedName("has_more")
    private boolean hasMore;

    public boolean isHasMore() {
        return hasMore;
    }
}
