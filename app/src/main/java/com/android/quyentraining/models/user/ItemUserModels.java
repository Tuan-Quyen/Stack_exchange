package com.android.quyentraining.models.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemUserModels implements Serializable {
    @SerializedName("items")
    private ArrayList<UsersModels> items = null;
    @SerializedName("has_more")
    private boolean hasMore;
    public boolean isHasMore() {
        return hasMore;
    }

    public ArrayList<UsersModels> getItems() {
        return items != null ? items : new ArrayList<UsersModels>();
    }
}
