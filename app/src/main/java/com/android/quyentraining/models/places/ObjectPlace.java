package com.android.quyentraining.models.places;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ObjectPlace implements Serializable {
    @SerializedName("geometry")
    private ObjectGeomatryPlace geomatryPlace;
    @SerializedName("name")
    private String nameLocation;
    @SerializedName("vicinity")
    private String address;
    @SerializedName("icon")
    private String photo;


    public String getPhoto() {
        return photo;
    }

    public String getAddress() {
        return address;
    }

    public String getNameLocation() {
        return nameLocation;
    }

    public ObjectGeomatryPlace getGeomatryPlace() {
        return geomatryPlace;
    }
}
