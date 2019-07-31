package com.android.quyentraining.activities.login;

import android.content.Context;
import com.android.quyentraining.connections.CheckConnection;

public class LoginPresenter{

    private LoginView loginView;
    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }
    public void guestionLogin(Context context){
        if (!CheckConnection.checkConnection(context)) {
            loginView.guestLoginNoInternet();
        } else {
            loginView.guestLoginHasInternet();
        }
    }
}
