package com.android.quyentraining.activities.splashscreen;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;

import com.android.quyentraining.R;
import com.android.quyentraining.base.BaseActivity;
import com.android.quyentraining.interfaces.ClickConnectionDialog;
import com.android.quyentraining.ultis.AppConstain;
import com.android.quyentraining.ultis.AppDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SplashScreenActivity extends BaseActivity implements ClickConnectionDialog, SplashScreenView {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDataBaseReference;
    AppDialog appDialog = new AppDialog();
    SplashScreenPresenter splashScreenPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
    }

    public void init() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        splashScreenPresenter = new SplashScreenPresenter(this, this, this);
        splashScreenPresenter.requestLocationPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        splashScreenPresenter.requestResult(requestCode, grantResults);
    }

    @Override
    public void setOnClickConnectionDialog() {
        this.finish();
    }

    @Override
    public void permission(String[] permission) {
        ActivityCompat.requestPermissions(this, permission, 1);
    }

    @Override
    public void hasConnection() {
        splashScreenPresenter.updateUI(user);
    }

    @Override
    public void noConnection() {
        appDialog.connectionErrorDialog(this, this);
    }

    @Override
    public void updateUI(FirebaseUser user, long now) {
        mDataBaseReference = firebaseDatabase.getReference().child(AppConstain.KEY_CHILD_USERS).child(user.getUid()).child(AppConstain.KEY_CHILD_TIME_ACTIVITY);
        mDataBaseReference.setValue(now);
    }
}
