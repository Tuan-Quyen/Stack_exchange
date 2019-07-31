package com.android.quyentraining.models.places;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ObjectLocationPlace implements Serializable {
    @SerializedName("lat")
    private Double lat;
    @SerializedName("lng")
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
