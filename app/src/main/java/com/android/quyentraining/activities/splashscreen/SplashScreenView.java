package com.android.quyentraining.activities.splashscreen;


import com.google.firebase.auth.FirebaseUser;

public interface SplashScreenView {
    void permission(String[] permission);
    void hasConnection();
    void noConnection();
    void updateUI(FirebaseUser user,long now);
}
