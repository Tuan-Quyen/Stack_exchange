package com.android.quyentraining.fragments.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.connections.APIConnection;
import com.android.quyentraining.connections.ConnectionRequest;
import com.android.quyentraining.models.places.ItemResultPlace;
import com.android.quyentraining.models.places.ObjectPlace;
import com.android.quyentraining.ultis.AppConstain;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MapPresenter {
    Context context;
    Activity activity;
    MapView mapView;

    public MapPresenter(Context context, Activity activity, MapView mapView) {
        this.context = context;
        this.activity = activity;
        this.mapView = mapView;

    }

    public void getLastknowLocation(boolean isGPS, boolean isNetwork) {
        if (!isGPS && !isNetwork) {
            mapView.noGPSAndNetWork(AppConstain.TOAST_TURN_ON_GPS);
        } else {
            if (isGPS) {
                mapView.getGPSLocation();
            } else if (isNetwork) {
                mapView.getNetWorkLocation();
            }
        }
    }

    public void locationChange(Location location, String errorString) {
        if (location != null) {
            mapView.locationChange(location);
        } else {
            mapView.noLocation(errorString);
        }
    }

    public Bitmap getMarkerBitmapFromView(String uri) {
        View customMarkerView;
        if (uri != null) {
            customMarkerView = ((LayoutInflater) activity.getSystemService(context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
            mapView.markerHasUrl(customMarkerView, uri);

        } else {
            customMarkerView = ((LayoutInflater) activity.getSystemService(context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.normal_marker, null);
            mapView.markerNoUrl(customMarkerView);
        }
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public Bitmap getMarkerCarBitmapFromView() {
        View customMarkerView = ((LayoutInflater) activity.getSystemService(context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView myImage = customMarkerView.findViewById(R.id.marker_imv);
        myImage.setImageResource((R.drawable.ic_directions_run_black_24dp));
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public void CallNearbyPlace(String type, LatLng latLng, long radius) {
        APIConnection apiConnection = ConnectionRequest.getRetrofit().create(APIConnection.class);
        Observable<ItemResultPlace> getNearbyPlace = apiConnection.getNearbyPlace(type, String.valueOf(latLng.latitude + "," + latLng.longitude), String.valueOf(radius)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        getNearbyPlace.subscribe(new Observer<ItemResultPlace>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ItemResultPlace itemResultPlace) {
                if(itemResultPlace != null) {
                    mapView.nearPlaceData(itemResultPlace);
                }else{
                    mapView.getDataPlaceFail(AppConstain.TOAST_NO_PLACE_FOUND);
                }
            }

            @Override
            public void onError(Throwable e) {
                mapView.nearPlaceDataFail(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void getDataPlace(List<ObjectPlace> objectPlaceNearByList, String error) {
        if (error == null) {
            for (int i = 0; i < objectPlaceNearByList.size(); i++) {
                Double lat = objectPlaceNearByList.get(i).getGeomatryPlace().getObjectLocationPlace().getLat();
                Double lng = objectPlaceNearByList.get(i).getGeomatryPlace().getObjectLocationPlace().getLng();
                mapView.getDataPlace(lat, lng, objectPlaceNearByList.get(i).getNameLocation(), objectPlaceNearByList.get(i).getAddress(), objectPlaceNearByList.get(i).getPhoto());
            }
        } else {
            mapView.getDataPlaceFail(error);
        }
    }
}
