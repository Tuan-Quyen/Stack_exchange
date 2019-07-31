package com.android.quyentraining.activities.splashscreen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import com.android.quyentraining.activities.main.MainActivity;
import com.android.quyentraining.activities.login.LoginActivity;
import com.android.quyentraining.connections.CheckConnection;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenPresenter {
    SplashScreenView splashScreenView;
    Context context;
    Activity activity;

    public SplashScreenPresenter(Context context, Activity activity, SplashScreenView splashScreenView) {
        this.splashScreenView = splashScreenView;
        this.context = context;
        this.activity = activity;
    }


    public void requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION};
            splashScreenView.permission(permissions);
            return;
        } else {
            checkConnection();
        }
    }

    public void requestResult(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    checkConnection();
                } else {
                    activity.finish();
                }
                break;
            }
        }
    }

    public void checkConnection() {
        if (CheckConnection.checkConnection(context)) {
            splashScreenView.hasConnection();
        } else {
            splashScreenView.noConnection();
        }
    }

    public void updateUI(final FirebaseUser user) {
        final int SPLASH_DISPLAY_LENGTH = 3000;
        final long now = System.currentTimeMillis() / 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    splashScreenView.updateUI(user, now);
                    Intent mainIntent = new Intent(activity, MainActivity.class);
                    context.startActivity(mainIntent);
                    activity.finish();
                } else {
                    Intent mainIntent = new Intent(activity, LoginActivity.class);
                    context.startActivity(mainIntent);
                    activity.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
