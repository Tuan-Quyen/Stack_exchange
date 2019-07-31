package com.android.quyentraining.fragments.map;

import android.location.Location;
import android.view.View;

import com.android.quyentraining.models.places.ItemResultPlace;

public interface MapView {
    void noGPSAndNetWork(String errorString);

    void getGPSLocation();

    void getNetWorkLocation();

    void locationChange(Location location);

    void noLocation(String errorString);

    void markerHasUrl(View view, String uri);

    void markerNoUrl(View view);

    void nearPlaceData(ItemResultPlace itemResultPlace);

    void nearPlaceDataFail(String errorString);

    void getDataPlace(double lat, double lng, String name, String address, String url);

    void getDataPlaceFail(String errorString);
}
