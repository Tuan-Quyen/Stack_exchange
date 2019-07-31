package com.android.quyentraining.models.places;

import com.android.quyentraining.models.places.ObjectPlace;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemResultPlace implements Serializable {
    @SerializedName("results")
    private List<ObjectPlace> objectPlaces = new ArrayList<ObjectPlace>();
    @SerializedName("error_message")
    private String error;

    public String getError() {
        return error;
    }

    public List<ObjectPlace> getObjectPlace() {
        return objectPlaces;
    }
}
