package com.android.quyentraining.models.site;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemSiteModels implements Serializable {
    @SerializedName("items")
    private ArrayList<YourSiteModels> items = null;
    public ArrayList<YourSiteModels> getItems() {
        return items != null ? items : new ArrayList<YourSiteModels>();
    }
    @SerializedName("has_more")
    private boolean has_more;

    public boolean isHas_more() {
        return has_more;
    }
}
