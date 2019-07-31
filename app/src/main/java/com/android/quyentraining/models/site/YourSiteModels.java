package com.android.quyentraining.models.site;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class YourSiteModels implements Serializable {
    @SerializedName("name")
    public String siteName;
    @SerializedName("icon_url")
    public String imvSite;
    @SerializedName("audience")
    public String audience;
    @SerializedName("api_site_parameter")
    public String api_site;
    @SerializedName("site_type")
    public String site_type;
    public String getSite_type() {
        return site_type;
    }

    public String getApi_site() {
        return api_site;
    }

    public String getAudience() {
        return audience;
    }

    public YourSiteModels() {
    }

    public String getSiteName() {
        return siteName != null ? siteName : "";
    }

    public String getImvSite() {
        return imvSite != null ? imvSite : "";
    }
}
