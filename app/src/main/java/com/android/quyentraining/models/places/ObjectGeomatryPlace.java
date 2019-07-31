package com.android.quyentraining.models.places;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ObjectGeomatryPlace implements Serializable {
    @SerializedName("location")
    private ObjectLocationPlace objectLocationPlace;

    public ObjectLocationPlace getObjectLocationPlace() {
        return objectLocationPlace;
    }
}
